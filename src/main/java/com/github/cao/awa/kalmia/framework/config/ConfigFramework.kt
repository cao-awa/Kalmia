package com.github.cao.awa.kalmia.framework.config

import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter
import com.github.cao.awa.apricot.resource.loader.ResourceLoader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.apricot.util.io.IOUtil
import com.github.cao.awa.kalmia.annotations.config.AutoConfig
import com.github.cao.awa.kalmia.annotations.config.AutoConfigTemplate
import com.github.cao.awa.kalmia.annotations.config.UseConfigTemplate
import com.github.cao.awa.kalmia.config.KalmiaConfig
import com.github.cao.awa.kalmia.config.inherite.InheritedValue
import com.github.cao.awa.kalmia.config.instance.ConfigEntry
import com.github.cao.awa.kalmia.config.template.ConfigTemplate
import com.github.cao.awa.kalmia.debug.dependency.circular.CircularDependency
import com.github.cao.awa.kalmia.debug.dependency.circular.RequiredDependency
import com.github.cao.awa.kalmia.env.KalmiaEnv
import com.github.cao.awa.kalmia.exception.auto.config.FieldParamMismatchException
import com.github.cao.awa.kalmia.exception.auto.config.WrongConfigTemplateException
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer
import org.apache.commons.codec.binary.StringUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.BiConsumer

class ConfigFramework : ReflectionFramework() {
    companion object {
        val LOGGER: Logger = LogManager.getLogger("ConfigFramework")
    }

    private val templatePaths: MutableMap<String, String> = ApricotCollectionFactor.hashMap()
    private val nameToTemplate: MutableMap<String, KalmiaConfig> = ApricotCollectionFactor.hashMap()
    private val templates: MutableMap<Class<out ConfigTemplate<*>>, ConfigTemplate<*>> =
        ApricotCollectionFactor.hashMap()
    private val configToTemplate: MutableMap<Class<out KalmiaConfig>, KalmiaConfig> =
        ApricotCollectionFactor.hashMap()

    override fun work() = loadTemplates()

    private fun extractDefaultTemplates() {
        val indexFile = File("./configs/index/index.json")
        if (!indexFile.isFile) {
            indexFile.parentFile.mkdirs()
            indexFile.createNewFile()
            IOUtil.write(
                FileOutputStream(indexFile),
                ResourceLoader.stream("kalmiagram/configs/index/index.json")
            )
        }

        extractDefaultTemplates(
            "configs/",
            JSONObject.parse(IOUtil.read(FileInputStream("./configs/index/index.json")))
        )

        loadPluginIndex("configs/index/plugin/", File("./configs/index/plugin"))
    }

    private fun loadPluginIndex(prefix: String, file: File) {
        if (!file.isDirectory) {
            return
        }
        for (index in file.listFiles()!!) {
            if (index.isDirectory) {
                loadPluginIndex("${prefix}${index.name}/", index)
            } else if (index.isFile) {
                loadPluginIndex(
                    "configs/plugin/",
                    JSONObject.parse(IOUtil.read(FileInputStream("./${prefix}${index.name}")))
                )
            }
        }
    }

    private fun loadPluginIndex(prefix: String, data: JSONObject) {
        for (entry in data) {
            val key = entry.key
            val value = entry.value
            if (value is JSONObject) {
                loadPluginIndex("$prefix$key/", value)
            } else {
                val location = "$prefix$value"
                this.templatePaths[key] = location
            }
        }
    }

    private fun extractDefaultTemplates(prefix: String, data: JSONObject) {
        File("./$prefix").mkdirs()

        for (entry in data) {
            val key = entry.key
            val value = entry.value
            if (value is JSONObject) {
                extractDefaultTemplates("$prefix$key/", value)
            } else {
                val location = "$prefix$value"
                val file = File("./$location")
                if (!file.isFile) {
                    EntrustEnvironment.trys({
                        file.createNewFile()
                        IOUtil.write(
                            FileOutputStream("./$location"),
                            ResourceLoader.stream(location)
                        )
                    }, { ex ->
                        LOGGER.warn(
                            "The template file for '{}' unable to extract",
                            key,
                            ex
                        )
                    })
                } else {
                    computeDefaultTemplate(file, location)
                }
                this.templatePaths[key] = location
            }
        }
    }

    private fun computeDefaultTemplate(file: File, location: String) {
        // 读取默认模板
        val defaultTemplate =
            JSONObject.parse(StringUtils.newStringUtf8(IOUtil.readBytes(ResourceLoader.stream(location))))
        // 读取当前模板
        val currentTemplate = JSONObject.parse(IOUtil.read(FileReader(file)))

        // 默认和当前模板都存在元数据时才能计算模板
        if (defaultTemplate.containsKey("metadata") && currentTemplate.containsKey("metadata")) {
            // 仅当配置版本不一致时计算模板
            if (defaultTemplate.getJSONObject("metadata")
                    .getInteger("version") !=
                currentTemplate.getJSONObject("metadata")
                    .getInteger("version")
            ) {
                // 写入当前模板没有的内容
                for (entry in defaultTemplate) {
                    if (!currentTemplate.containsKey(entry.key)) {
                        currentTemplate[entry.key] = entry.value;
                    }
                }

                // 删除当前模板多余的内容
                for (entry in currentTemplate) {
                    if (!defaultTemplate.containsKey(entry.key)) {
                        currentTemplate.remove(entry.key)
                    }
                }

                // 更新配置版本
                currentTemplate["metadata"] = defaultTemplate["metadata"]
            }
        }

        // 写入当前模板
        IOUtil.write(
            FileOutputStream("./$location"),
            currentTemplate.toString(JSONWriter.Feature.PrettyFormat)
        )
    }

    private fun loadTemplates() {
        extractDefaultTemplates()

        reflection().getTypesAnnotatedWith(AutoConfigTemplate::class.java)
            .filter { template ->
                template == ConfigTemplate::class.java
                        || ConfigTemplate::class.java.isAssignableFrom(template)
            }
            .filter(Objects::nonNull)
            .map(this::cast)
            .forEach { templateClass ->
                // 获取此配置的模板路径
                val templateData = templateClass.getAnnotation(AutoConfigTemplate::class.java)

                // 获取实际类型然后创建实例
                val configType: Class<out KalmiaConfig> =
                    EntrustEnvironment.cast(toClass(getArgType(templateClass.genericSuperclass)))!!
                val config = configType.getConstructor().newInstance() as KalmiaConfig

                // 读取模板格式
                val json = EntrustEnvironment.get({
                    JSONObject.parse(
                        IOUtil.read(
                            FileReader(
                                this.templatePaths[templateData.value],
                                StandardCharsets.UTF_8
                            )
                        )
                    )
                }, JSONObject.of())

                // 创建模板
                try {
                    // 当类型不正确时构建异常链，用以debug
                    EntrustEnvironment.reThrow(
                        { createTemplate(config, json, CircularDependency()) },
                        FieldParamMismatchException::class.java,
                        { WrongConfigTemplateException(templateClass, it.field, it) }
                    )
                } catch (ex: WrongConfigTemplateException) {
                    LOGGER.warn(
                        "Failed to resolve the template '{}' for config '{}'",
                        templateClass.simpleName,
                        config::class.java.simpleName,
                        ex
                    )
                }
                val template = templateClass.getConstructor().newInstance() as ConfigTemplate<*>

                // 给模板设置配置内容并存储备用
                fetchField(template, "config").set(template, config)
                this.templates[templateClass] = template
                this.configToTemplate[configType] = config
                this.nameToTemplate[templateData.value] = config
            }
    }

    private fun cast(clazz: Class<*>): Class<out ConfigTemplate<*>> = EntrustEnvironment.cast(clazz)!!

    private fun createTemplate(
        target: KalmiaConfig,
        getter: JSONObject,
        configChain: CircularDependency
    ) {
        prepareToHandles(target, "", null) { field, _ ->
            // 确保ConfigEntry不为空
            val configEntry = ensureEntryNotNull(target, field)

            // 获取配置要求的类型
            val argType = getArgType(field)

            val useTemplate = field.getAnnotation(AutoConfig::class.java)

            val inTemplateFileKey = if (useTemplate.equals("")) {
                field.name
            } else {
                useTemplate.value
            }

            postProcessing(target, configEntry, argType, configChain,
                { parameterized ->
                    // 当依赖是泛型而不是Entry也不是数据

                    // ConfigEntry只能有一层泛型，不允许List<List<List<...>>>
                    val clazz = toClass(parameterized.rawType)

                    // 如果是集合，只创建array list和hash set，其他特殊指定类型会被忽略
                    if (Collection::class.java.isAssignableFrom(clazz)) {
                        var newDelegate: MutableCollection<Any>? = null
                        if (List::class.java == clazz) {
                            newDelegate = ApricotCollectionFactor.arrayList()
                        }
                        if (Set::class.java == clazz) {
                            newDelegate = ApricotCollectionFactor.hashSet()
                        }

                        // 确保已经创建了集合对象
                        if (newDelegate == null) {
                            return@postProcessing
                        }

                        // 获得实际的类型
                        val listTemplate = toClass(parameterized.actualTypeArguments[0])

                        // 添加数据并设置ConfigEntry的值
                        for (value in getTemplateJsonArray(getter, field)) {
                            // 当实际类型是一个依赖时，进一步创建，否则在检查后直接添加
                            if (KalmiaConfig::class.java.isAssignableFrom(listTemplate)) {
                                newDelegate.add(
                                    createTemplateObject(
                                        listTemplate,
                                        value as JSONObject,
                                        configChain
                                    )
                                )
                            } else {
                                // 当类型不正确时构建异常链，用以debug
                                EntrustEnvironment.reThrow(
                                    { checkType(listTemplate, value, newDelegate::add) },
                                    ClassCastException::class.java,
                                ) { FieldParamMismatchException(field, listTemplate, value::class.java, it) }
                            }
                        }
                        fetchField(configEntry, "value").set(configEntry, newDelegate)
                    }

                    // 如果是Map，只创建hash map，其他特殊指定类型会被忽略
                    if (Map::class.java.isAssignableFrom(clazz)) {
                        if (parameterized.actualTypeArguments[0] != String::class.java) {
                            throw IllegalArgumentException("The config entry can only use 'java.lang.String' come as the key type")
                        }

                        // 创建并确保已经创建了集合对象
                        val newDelegate: MutableMap<Any, Any> = ApricotCollectionFactor.hashMap()

                        // 添加数据并设置ConfigEntry的值
                        for (entry in getTemplateJsonData(getter, field)) {
                            newDelegate[entry.key] = entry.value.toString()
                        }
                        fetchField(configEntry, "value").set(configEntry, newDelegate)
                    }
                },
                {
                    // 当依赖是Entry而不是数据

                    // 处理此配置的依赖
                    // 配置对象内所有字段都应为ConfigEntry<KalmiaConfig>
                    if (isTemplateDataPresent(getter, field)) {
                        try {
                            val templateData = getTemplateData(getter, field)

                            // @INHERITED 用于"指引"这个数据应该从哪个模板的默认数据获取
                            // 它没有实际的配置意义，只是用来指引数据来源，避免一些适合在生产环境查代码
                            if (templateData is String && templateData.toString().startsWith("@INHERITED")) {
                                val indicates = templateData.substring(11)
                                if (this.templatePaths[indicates] == null) {
                                    LOGGER.warn(
                                        "The template '{}' that indicated by '{}' in template '{}' has not found, may not be indicated correctly",
                                        indicates,
                                        inTemplateFileKey,
                                        target::class.java.simpleName
                                    )
                                }
                                return@postProcessing
                            }

                            // 处理此配置要求的其他依赖
                            createTemplate(
                                configEntry.get() as KalmiaConfig,
                                getTemplateJsonData(getter, field),
                                configChain
                            )
                        } catch (ignored: Exception) {
                            // 错误时不要处理，后续的创建会自动换为这一字段的另一个模板
                            // 成功：后续创建遇到此字段都从这拿
                            // 失败：后续创建遇到此字段都从这个字段的模板拿
                            // 其实就是一个覆盖的功能，组装时优先选择来自同一个文件的
                        }
                    } else {
                        // 不存在"指引"也不存在实际的数据时提示，不影响后续解析
                        LOGGER.warn(
                            "The template file of '{}' has missing required data '{}' that should inherited from '{}'",
                            target::class.java.simpleName,
                            inTemplateFileKey,
                            toClass(getArgType(field)).simpleName
                        )
                    }
                }
            ) {
                // 当依赖是数据而不是Entry

                val requiredType = toClass(argType)
                val value = getTemplateData(getter, field)

                // 此处的 @INHERITED 与ConfigEntry的完全不同，它会实际地从所指引的目标获取值
                if (value is String && value.startsWith("@INHERITED")) {
                    val inheritedData = value.substring(11).split("#")
                    val inheritedTemplate = inheritedData[0]
                    val targetKey = if (inheritedData.size > 1) inheritedData[1] else inTemplateFileKey
                    fetchField(configEntry, "value")[configEntry] = InheritedValue(inheritedTemplate, targetKey)
                    return@postProcessing
                }

                // 当类型不正确时构建异常链，用以debug
                EntrustEnvironment.reThrow(
                    {
                        checkType(
                            requiredType,
                            value
                        ) { fetchField(configEntry, "value")[configEntry] = it }
                    },
                    ClassCastException::class.java,
                ) { FieldParamMismatchException(field, requiredType, value::class.java, it) }
            }
        }
    }

    private fun createTemplateObject(
        configType: Class<*>,
        value: JSONObject,
        configChain: CircularDependency
    ): KalmiaConfig {
        val config = configType.getConstructor().newInstance() as KalmiaConfig
        createTemplate(
            config,
            value,
            configChain
        )
        return config
    }

    private fun isTemplateDataPresent(getter: JSONObject, field: Field): Boolean {
        return getter.containsKey(field.name) || getter.containsKey(field.getAnnotation(AutoConfig::class.java).value)
    }

    private fun getTemplateData(getter: JSONObject, field: Field): Any {
        var data = getter.get(field.name)
        if (data == null) {
            data = getter.get(field.getAnnotation(AutoConfig::class.java).value)
        }
        return data
    }

    private fun getTemplateJsonData(getter: JSONObject, field: Field): JSONObject {
        var data = getter.getJSONObject(field.name)
        if (data == null) {
            data = getter.getJSONObject(field.getAnnotation(AutoConfig::class.java).value)
        }
        return data
    }

    private fun getTemplateJsonArray(getter: JSONObject, field: Field): JSONArray {
        var data = getter.getJSONArray(field.name)
        if (data == null) {
            data = getter.getJSONArray(field.getAnnotation(AutoConfig::class.java).value)
        }
        return data
    }

    /**
     * @param target 此参数为要处理的对象
     * @param parentTemplate 此模板用以覆盖某种模板的默认数据，用在特定的模板中
     * @param handler 在进行预处理完成后会被调用，进行中间处理
     *
     * @see postProcessing
     */
    private fun prepareToHandles(
        target: Any,
        currentKey: String,
        parentTemplate: KalmiaConfig?,
        handler: BiConsumer<Field, KalmiaConfig?>
    ) {
        val clazz = target.javaClass

        // 获取此配置对象使用的模板
        val useTemplate = getAnnotation(target, UseConfigTemplate::class.java)
        val configTemplate = useTemplate?.value?.java?.let { getTemplate(it) }

        var usedTemplate = fetchTemplateConfig(configTemplate)

        var hasInherited = false

        // 当parentTemplate存在时，这意味着当前的配置的模板重写了目标模板
        if (parentTemplate != null && usedTemplate != null) {
            for (field in getFields(parentTemplate)) {
                val configEntryField = fetchField(
                    parentTemplate,
                    field.name
                )
                if (getArgType(configEntryField) == usedTemplate!!::class.java && (field.name.equals(currentKey))) {
                    val configEntry = (configEntryField[parentTemplate] as ConfigEntry<*>).get()
                    if (configEntry != null) {
                        usedTemplate = configEntry as KalmiaConfig
                        hasInherited = true
                    }
                }
            }
        }

        if (usedTemplate != null) {
            for (field in getFields(usedTemplate)) {
                if (fetchField(target, field.name) == null) {
                    throw IllegalStateException("The '" + clazz.name + "' doesn't match to the config template '" + useTemplate.value.java.name + "'")
                }
            }
        }

        Arrays.stream(
            clazz.declaredFields
        ).filter {
            it.isAnnotationPresent(AutoConfig::class.java)
        }.filter(
            Objects::nonNull
        ).forEach {
            // 当声明的自动配置字段不是ConfigEntry时不做处理
            if (it.type != ConfigEntry::class.java && !ConfigEntry::class.java.isAssignableFrom(it.type)) {
                LOGGER.warn(
                    "The field '{}' is not ConfigEntry, unable to be process",
                    it.name
                )
                return@forEach
            }
            if (!Modifier.isFinal(it.modifiers)) {
                LOGGER.warn(
                    "The field '{}' declared in '{}' is not final modified, may cause explicit changes",
                    it.name,
                    target::class.java.name
                )
            }
            ensureAccessible(it)

            var template = usedTemplate

            if (hasInherited && usedTemplate != null) {
                val configEntry = fetchField(
                    usedTemplate,
                    it.name
                )
                if (KalmiaConfig::class.java.isAssignableFrom(
                        toClass(
                            getArgType(
                                fetchField(
                                    usedTemplate,
                                    it.name
                                )
                            )
                        )
                    ) && getArgType(configEntry) == usedTemplate::class.java
                ) {
                    template = (fetchField(
                        usedTemplate,
                        it.name
                    )[usedTemplate] as ConfigEntry<*>).get() as KalmiaConfig
                }
            }

            handler.accept(it, template)
        }
    }

    fun getTemplate(o: Any): KalmiaConfig? {
        return fetchTemplateConfig(
            getAnnotation(
                o,
                UseConfigTemplate::class.java
            )?.value?.java?.let { getTemplate(it) }
        )
    }

    private fun fetchTemplateConfig(configTemplate: ConfigTemplate<*>?): KalmiaConfig? {
        return if (configTemplate != null) fetchField(
            configTemplate,
            "config"
        )[configTemplate] as KalmiaConfig else null
    }

    private fun <T : ConfigTemplate<*>> getTemplateConfig(configTemplate: Class<T>): KalmiaConfig? {
        return fetchTemplateConfig(getTemplate(configTemplate))
    }

    fun createConfig(o: Any) {
        createConfig(o, "", getTemplate(o), CircularDependency())
    }

    private fun createConfig(
        target: Any,
        currentKey: String,
        parentTemplate: KalmiaConfig?,
        configChain: CircularDependency
    ) {
        prepareToHandles(target, currentKey, parentTemplate) { field, template ->
            // 确保ConfigEntry不为空不
            val configEntry = ensureEntryNotNull(target, field)

            val argType = getArgType(field)

            val useTemplate = field.getAnnotation(AutoConfig::class.java)

            val inTemplateFileKey = if (useTemplate.equals("")) {
                field.name
            } else {
                useTemplate.value
            }

            val creatingWithTemplate = {
                // 当依赖是数据而不是Entry

                // 读取模板获得默认值
                val value = if (template != null) {
                    val requiredType = toClass(argType)

                    // 首先从当前配置模板中获取
                    val fetchedTemplate = fetchField(template, field.name)[template]
                    var fetchResult: Any? = null
                    if (fetchedTemplate != null && fetchedTemplate != ConfigEntry.ENTRY) {
                        fetchResult = checkOrDiscard(
                            requiredType,
                            (fetchedTemplate as ConfigEntry<*>).get(),
                            InheritedValue::class.java
                        )
                    }

                    // 当前模板找不到时从父模板的默认数据板尝试获取
                    // 因为父模板可能覆盖了这个模板的默认值
                    if (parentTemplate != null && fetchResult == null) {
                        fetchResult = checkOrDiscard(
                            requiredType,
                            fetchConfigValue(
                                // 使用currentKey获取父模板的默认数据
                                fetchConfig(
                                    this.configToTemplate[parentTemplate::class.java]!! as? KalmiaConfig,
                                    currentKey
                                ),
                                field
                            ),
                            InheritedValue::class.java
                        )
                    }

                    // 若当前配置模板和父模板中都不存在需要的目标，则使用此模板的默认数据
                    if (fetchResult == null) {
                        // 不要忘记fetch到需要的字段后get，这个默认数据是KalmiaConfig而不是此字段的值()
                        fetchResult = checkOrDiscard(
                            requiredType,
                            fetchConfigValue(
                                this.configToTemplate[template::class.java]!!,
                                field
                            ),
                            InheritedValue::class.java
                        )
                    }

                    // 当获取结果是InheritedValue时说明它引用了其他模板的默认数据
                    if (fetchResult is InheritedValue) {
                        val inheritedValue = fetchResult
                        val config: KalmiaConfig? = this.nameToTemplate[inheritedValue.templateName]
                        if (config == null) {
                            LOGGER.warn(
                                "The inherited data '{}' arent present that inherited by '{}' in template '{}'",
                                inheritedValue.templateName,
                                inTemplateFileKey,
                                target::class.java.simpleName
                            )
                        } else {
                            fetchResult = EntrustEnvironment.get({
                                fetchOrFindEntry(config, inheritedValue.key).get()
                            }, null)
                        }
                    }

                    if (fetchResult == null) {
                        LOGGER.warn(
                            "The data '{}'(named '{}') in config entry of '{}' could not be found or created",
                            field.name,
                            inTemplateFileKey,
                            target::class.java.simpleName
                        )
                    }

                    // 返回结果
                    fetchResult
                } else {
                    // 这边没有template，所以永远是null
                    null
                }

                // 当值存在时则设定
                if (value != null) {
                    checkType(
                        toClass(argType),
                        value
                    ) { fetchField(configEntry, "value").set(configEntry, it) }
                }
            }

            postProcessing(
                target, configEntry, argType, configChain,
                // 处理泛型的方式和处理普通数据一样
                { creatingWithTemplate() },
                {
                    // 当依赖是Entry而不是数据

                    // 处理此配置的依赖
                    // 配置对象内所有字段都应为ConfigEntry<KalmiaConfig>
                    val config = configEntry.get() as KalmiaConfig
                    createConfig(config, configEntry.key()!!, template ?: getTemplate(config), configChain)
                },
                // 处理普通数据
                creatingWithTemplate
            )
        }
    }

    private fun fetchOrFindEntry(target: Any, key: String): ConfigEntry<*> {
        val field = fetchField(target, key)
        if (field == null) {
            for (renamedField in getFields(target).filter {
                it.isAnnotationPresent(AutoConfig::class.java)
            }) {
                if (renamedField.getAnnotation(AutoConfig::class.java).value == key) {
                    return renamedField[target] as ConfigEntry<*>
                }
            }
        }
        return field[target] as ConfigEntry<*>
    }

    private fun fetchConfig(template: KalmiaConfig?, field: Field): KalmiaConfig? = fetchConfig(template, field.name)

    private fun fetchConfig(template: KalmiaConfig?, field: String): KalmiaConfig? {
        val fetchedSourceTemplate = fetchField(template ?: return null, field)[template] ?: return null
        val result = (fetchedSourceTemplate as ConfigEntry<*>).get() as? KalmiaConfig
            ?: return null
        return result
    }

    private fun fetchConfigValue(template: KalmiaConfig?, field: Field): Any? = fetchConfigValue(template, field.name)

    private fun fetchConfigValue(template: KalmiaConfig?, field: String): Any? {
        val fetchedSourceTemplate = fetchField(template ?: return null, field)[template] ?: return null
        return (fetchedSourceTemplate as ConfigEntry<*>).get()
    }

    private fun postProcessing(
        target: Any,
        configEntry: ConfigEntry<*>,
        argType: Type,
        configChain: CircularDependency,
        actionWhenParameterized: Consumer<ParameterizedType>,
        actionWhenEntry: Runnable,
        actionWhenData: Runnable
    ) {
        // 当ConfigEntry为空时创建其依赖
        if (configEntry.get() == null) {
            if (argType is ParameterizedType) {
                // 当依赖是泛型而不是Entry也不是数据
                actionWhenParameterized.accept(argType)
            } else {
                val actualClass = toClass(argType)

                // 当依赖是Entry而不是数据
                if (KalmiaConfig::class.java.isAssignableFrom(actualClass)) {
                    // 设置新的配置对象
                    fetchField(configEntry, "value").set(configEntry, actualClass.getConstructor().newInstance())

                    // 提交当前的依赖项，同时检查是否循环依赖
                    // 用以快速打断循环依赖，避免发生StackOverflowError
                    configChain.pushRequirement(target.javaClass.name, RequiredDependency().add(actualClass.name))

                    actionWhenEntry.run()
                } else {
                    // 当依赖是数据而不是Entry
                    actionWhenData.run()
                }
            }
        }
    }

    private fun getTemplate(clazz: Class<out ConfigTemplate<*>>): ConfigTemplate<*>? {
        return this.templates[clazz]
    }

    private fun ensureEntryNotNull(target: Any, field: Field): ConfigEntry<*> {
        val fieldValue = field[target]
        var configEntry: ConfigEntry<*>? =
            if (fieldValue == null || fieldValue == ConfigEntry.ENTRY) null else fieldValue as ConfigEntry<*>
        configEntry = configEntry ?: field.type.getConstructor().newInstance() as ConfigEntry<*>
        // 设置此ConfigEntry的key为字段名称，要用它来获取多个相同模板的数据以及debug
        fetchField(configEntry, "key")[configEntry] = field.name
        // 将配置设置回字段
        fetchField(field)[target] = configEntry
        return configEntry
    }

    fun <T : Any> deepCopy(o: T, another: T) {
        deepCopy(o, another, CircularDependency())
    }

    private fun <T : Any> deepCopy(o: T, another: T, configChain: CircularDependency) {
        val clazz = o::class.java

        Arrays.stream(
            clazz.declaredFields
        ).filter {
            it.isAnnotationPresent(AutoConfig::class.java)
        }.filter(
            Objects::nonNull
        ).forEach {
            // 当声明的自动配置字段不是ConfigEntry时不做处理
            if (it.type != ConfigEntry::class.java && !ConfigEntry::class.java.isAssignableFrom(it.type)) {
                LOGGER.warn(
                    "The field '{}' is not ConfigEntry, unable to be process",
                    it.name
                )
                return@forEach
            }
            // 确保ConfigEntry不为空不
            val fieldValue = it[o]
            val configEntry = if (fieldValue == null) null else fieldValue as ConfigEntry<*>?
            if (configEntry == null) {
                return@forEach
            }
            // 先创建用来复制的ConfigEntry
            val newConfigEntry = it.type.getConstructor().newInstance() as ConfigEntry<*>
            // 设置此ConfigEntry的key为字段名称
            fetchField(newConfigEntry, "key").set(newConfigEntry, it.name)
            // 将ConfigEntry设置到新字段
            fetchField(another, it.name).set(another, newConfigEntry)

            // 将配置内容深拷贝到新的ConfigEntry
            postProcessing(o, newConfigEntry, getArgType(it), configChain,
                {
                    // TODO 泛型复制
                },
                {
                    // 当依赖是Entry而不是数据

                    // 处理此配置的依赖
                    // 配置对象内所有字段都应为ConfigEntry<KalmiaConfig>
                    val oldConfig = configEntry.get() as KalmiaConfig
                    val newConfig = newConfigEntry.get() as KalmiaConfig
                    deepCopy(oldConfig, newConfig, configChain)
                },
                {
                    // 使用序列化器破坏引用
                    EntrustEnvironment.notNull(
                        fetchField(newConfigEntry, "value")
                    ) { field ->
                        field.set(
                            newConfigEntry,
                            KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.breakRefs(configEntry.get() ?: return@notNull)
                        )
                    }
                }
            )
        }
    }
}
