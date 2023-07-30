import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.Mathematics;

import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class MiscellaneousManualTest {
    public static void main(String[] args) {
        try {
//            System.out.println("---Test start---");
            long start = TimeUtil.millions();
//
//            rsa();
//
//            System.out.println(TimeUtil.processMillion(start) + "ms");
            start = TimeUtil.millions();

            aes();

            System.out.println(TimeUtil.processMillion(start) + "ms");

//            MessageManager messageManager = new MessageManager("data/msg");
//
////            for (int i = 0;i < 500;i++) {
////                messageManager.send(123456, new PlainMessage(RandomIdentifier.create(16384), 1234));
////            }
//            List<Message> messages = ApricotCollectionFactor.newArrayList();
//            messageManager.operation(123456,
//                                     0,
//                                     200,
//                                     (seq, msg) -> {
//                                         messages.add(msg);
//                                     }
//            );
//
////            SendMessageRequest request = new SendMessageRequest(123456, BytesRandomIdentifier.create(16), new PlainMessage("wwwwww", 114514).toBytes());
//            SelectedMessageRequest request = new SelectedMessageRequest(123456,
//                                                                        0,
//                                                                        200,
//                                                                        messages
//            );
//            long startTime = TimeUtil.millions();
//            byte[] source = request.data();
//            long encodingSourceTime = TimeUtil.processMillion(startTime);
//
//            startTime = TimeUtil.millions();
//            byte[] deflate = DeflaterCompressor.INSTANCE.compress(request.data());
//            long encodingDeflateTime = TimeUtil.processMillion(startTime);
//
//            startTime = TimeUtil.millions();
//            byte[] zstd = ZstdCompressor.INSTANCE.compress(request.data());
//            long encodingZstdTime = TimeUtil.processMillion(startTime);
//
//            // Decode test
//            startTime = TimeUtil.millions();
//            DeflaterCompressor.INSTANCE.decompress(deflate);
//            long decodingDeflateTime = TimeUtil.processMillion(startTime);
//
//            startTime = TimeUtil.millions();
//            ZstdCompressor.INSTANCE.decompress(zstd);
//            long decodingZstdTime = TimeUtil.processMillion(startTime);
//
//            new File("test/").mkdirs();
//
//            BufferedOutputStream resultOut = new BufferedOutputStream(new FileOutputStream("result.txt"));
//
//            IOUtil.write0(resultOut, "----sizes----\n");
//            IOUtil.write0(resultOut, "source: " + source.length + " bytes in " + encodingSourceTime + "ms (decode N/A ms)\n");
//            IOUtil.write0(resultOut, "deflate: " + deflate.length + " bytes in " + encodingDeflateTime + "ms (decode " + decodingDeflateTime + "ms)\n");
//            IOUtil.write0(resultOut, "zstd: " + zstd.length + " bytes in " + encodingZstdTime + "ms (decode " + decodingZstdTime + "ms)\n");
//
//            IOUtil.write0(resultOut, "----data----\n");
//            IOUtil.write0(resultOut, source.length + ": " + Arrays.toString(source) + "\n");
//            IOUtil.write0(resultOut, deflate.length + ": " + Arrays.toString(deflate) + "\n");
//            IOUtil.write(resultOut, zstd.length + ": " + Arrays.toString(zstd) + "\n");

//            JSONObject json = new JSONObject();
//            json.put("a", "bc");
//
//            System.out.println(json.toString());
//            System.out.println(json.toString(JSONWriter.Feature.PrettyFormat));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void aes() throws Exception {
        byte[] cipher = BytesRandomIdentifier.create(64);

        byte[] plaintext = "www".getBytes();

        byte[] ciphertext = Crypto.aesEncrypt(plaintext,
                                              cipher
        );

        System.out.println(new String(ciphertext));

        byte[] decrypted = Crypto.aesDecrypt(ciphertext,
                                             cipher
        );

        System.out.println(new String(decrypted));
    }

    public static void rsa() throws Exception {
        KeyPair pair = Crypto.rsaKeypair(4096);
        System.out.println(Mathematics.radix(pair.getPublic()
                                                 .getEncoded(),
                                             36
        ));
        System.out.println(Mathematics.radix(pair.getPrivate()
                                                 .getEncoded(),
                                             36
        ));

        RSAPublicKey publicKey = Crypto.decodeRsaPubkey(Mathematics.toBytes("nz02b2aac6hyht2sw5ry3hiyfi3h2xlgruxvpuolw2cg4rvut0mog6y0y3isvvy05txuvjbra02e1i6g8rwdtzvyewodtpik1rekl65sr37kw6748qvr4dwc5ptv53hbhtx4ray6szcj89xwmotbuxoy9sp2wqgqyymcgc1k7rbfrvvo9a8sad2pgll80du4zy8julo8rkijicxxx40eq61pinmp7tp1t75uns9todd8tu4jx6opvoflfno6qxhaccerfmo6u4cv9ajnialmv2lmhcruu6nmhfrhwskbp3h872j68xatl8r20r2h55eg5s7ncovtp4lukblnj539sfo9lfgzlfdwq81iokuwqoe0s29q1ff0dtublxkg062itejvycyfo7ft5le25elbkyjietesixq4t84ztwxjyluf4jvoten2acdv0qrv67fxixmumsockgbzl1988yjfrlh4pfqky6ovj6u2mv0dqusfgc2jbxon2gh3z1nd75f2n4e87mxu1pwk3gih1szf5ahjlkxwppa1a2ofj3q749x7h1c3rfo6k4z9touh44fzjw9uqdhvnq3euwnajiv7qexsww2k3aftkp8gkekqcozj5batdx834rqmihqykrv6u38m85lshxgotghuk9pbkswbl5eui6awbilaf90zc6k9601ms33ac19bz1sbcmqf1hdf4qwwcg7alt6twr5wixb10j6hrc9q14qsv8grtr6r9fj2b84o0m54fdyo2u5a62jcj2nin6bwyv9gchi4lc40nmqklgz80k108925eg0qn0bxybswnkdd59uea0ubs6x81mxxntizla58j5z99y8ehe6rwb1ok3mvadd28b8mjmnz7l2tl419fr8ohvuan6yzfh53gi7naqzle3nwo098e6ff5nb135msreqagkyyphxsgc029lmq5by69x2kkvrfyzdh13zivcn4ivre4pyto8lb9brkkwusmsrpakvh436kovxz1jzo5jpliazu0erp0wb9o6mlod6f639hghmnj218l3lfkuauxakn4kaltyoma2b6ws7jlcjbvrr451x875bb1gvp96lh05rvvzzkd3j9g28eo023l32aixwhvalggl2g30e7gdzn1k38q909p5sdc4eeh70oxllc7qjzbt6ee1c6pb7utp4ovmx3f4vpidr0qcjydgvo65zewb4bu4i1yldfmlj5t0ptmjxkmz8vkr7ouryewl0kef1kxrqehqjtgwesp8901jxd55jmqx4ffy3u4i4rar4wqdmuzo6m8r6yph6ld9icrqnr6c3pyzjw9db7ipnkw930kf838mgz4nb3sz34b9if0ixktaycpn3dr7jstwdkk9e50k5zofpkoaj1c93rdufeb8yy09lzajafung1gu1oe271evbdnqr0pl8g8ok1pxpp4ksnsn44zuu4qakg11osc5glcwsbdh5pes2teek0xgiuieoznqpgeuq4rthl6se1idv0zupru0j1k4zdlshjs8spc73m8jwryt69ogcfzkcctorjxlxwqgmmtq4ps358o0djmspch28st8af2h5l59nye8xrcqmblzt137g7yx4s0splc1zlz36y730o2rl",
                                                                            36
        ));
        RSAPrivateKey privateKey = Crypto.decodeRsaPrikey(Mathematics.toBytes("mtzyvm2ov17ukv59pt550bk7hr8ocql88rd41kpphx91fmq5d1nq1wa8hu5wawgaonpv594gkfhe43pgfnpm6ad2jmi88ma1r2e3662ng3v0kf0anr18uza3keu8lvp5jqwmrd6scvexb0difej7dszrh9td3n59w28x0mqddfm2or7tx4u6xm8lw2xndm5lgo2jmure5am7zatgoi3rqslxyuz566y74hlbweq6hgraj0ccsf8mnyh9y20b7rs8y4amdkyp0r8trhr2fqybcmq5g2rrwcgfd25ei4z07jrmh76z5l442v0batu8yrguds8eljsticeujftagtgqvdg5x19syj071j541s7j3in6eixgp5j43h3a8am2gyw74xja56t9hygcx7p6pqgv3jhgqm7jezauxyj1whdch4dkhzkkh4bojtl5fuc5adxfbl0dp6wopunvfokmbsxrm3ceo5cdmosk5pi372ed651l2hgrxe1plb1x7op6x3qp73o686jtyutk9n29dsb8bprm0xgp0szjobj6cl4lgv29jw5ezeaszms8v6hs7en80vkcgxh58fsv0tsb1o1clrylwxjmhvuikfnxudjw8rliyu4tnto4ach540tlidibjnlqa32kclke4k0rwsxq65hq6bd8a0lpc6v3c9d7wfcyilrb10qu9o7tbzx290trbb61o8jl8sqvb10kynpawti3ztftpfsnjk5sfnmxfpmedjbiz1kfchuhqo49wxttby4yk5yz8je3ngz1a0vdx1jyrabidbtv21xcz9do8t066auqty4xxzgaz2ruzlpajjoconnfbam87baepvivco5us8y8wh2u6iae9z1zkrcol22x123vept5kwhzw6awyfdlzjqcwdkffaqils89o0xxuzmhpiyb7nznhibrw305w7yz3htxfpfyt35fv2hnoy545z3keb2799c92ai2eeuppll7xz0ksncitc90izseufrjmrjmngh1agnnpz5gqg4c25hbrdy8iwdblw3qmzaks8jv6iuefyfacrf5qgtdqhals6eor0xw9klygp7zj9v7hbds8co3b26b0bstphvgerhyt3dlg6asgr4zh6ma0rwdm4rurg3d33cdvp03q64vovwpzpnup9gqa8crt6f8crvtm55hg1vthvuhp8f8nlt94694rhkqvbrl0n6jbdz2c9h2opc2jsz21j8bgdkfg6qdv70c2hp8pmk4i92so5t67q7utyqhjxp3lme1egqmwhe8lx0dx5lds5ic7xxswrjxqpo2v6w5drdmcbj0hue5trrh5cnomz5ndey043t9guh5zry4yqvl55pelx9j8qn49qyf1cfadim9dl1d2s08l3tbxqdi3srau35y521cf3qi4ldhr1vdbk54wl5k2ejhtyzit6zrsr21c09yffkdtnwjiohygkh7frriif62ikwqxw83dpuu5hz1pjrrvd45m9g08q69phiw7cx9w26efxvkeaprz6etbf95xjrhw6w20fdt301ahcipbmnemow40p9uykyc5eax2484seqg4q1es9gfhckgyqyxgnh7aupmf3va9zw29gtuby5sk408no5jznysiz1491hhkpg425lc7vf2yvd3vz65g32dz1dki6v0j3gk6wk3i1mscsc4473vebfdlkbkab3fqbwsgp40pwor1n5wxuzfk019wv4bz4magybkrbb74b3ccpmdjbu21481por6ydow3t96duwl1jp9bpjdaapzxqe7o20fyr6yyvqfqgugc86tykvovlgerwd5dcvlxcpiym2vlq4gtayjga85embescjiovcfz2fi0e0y1tknwqg6v7u82c5jaiu2hlf9zfh9yvd4henus6iv3914anpludz2n3uyxzrej3xonxzgh1zb1trehh2dvw9yzf2hjx5gsa9iwgzbkeeftzgua598t3z8hlkhhjxttea6ehc6rr05u17re3d6l4rq619c6hfv0dwk0adjqvkpi00cjgtk4o5ng31etc7azrgewia7pho4g9a0safshki52leltbqhbq7syc301wald133z5bwotxux9yoh9ox7p9kqv44moa4em3w0mcoijzbgyc2mb4wnkvcvo3p589ardylkd51ia0hr7nry9hdodiw6rld4c1sfxisa915hjt58q4yvg6ygdz0pcwc3649j4vzag43ujp89agsbx3xlxvw88tqp7sfufykjzp99cldwmbxnlnl8goxp8mnzpsjgw0kpyo217q9q78gkrqo0uz2g68k55iuvx3rmdxuo72iv15cas7dfcgvdhiz04j03b17zxouq1avl10e06j82c8zbk8e4cu3s29d74dpanlkny75c5k25oue0b9ggxnn1honhhu5wmh5qyh0hsdtpb6qwhmfwb56d1yffbtlaglo8c4x7dvswwymatp29zeo9nmhy55nm8avqebfokoiiwqwl9mt2gcsz618cwook9a777092gsd0m8jri3w3dp2n214wxj5ndlk6360au2dqdritgphrotxch9m6vfoxfmhu1vqf7lmtz9lbsgagrzefqy7puz9l8b9urultxwugy5z7o285uga3uv4djhmx9hqs7mvu3g3mo73i4mrdt1c2517weimwnukcmx6lpes8kuz9uuswxp8bpzu4plzcsnst0l75o9gkztn94me3zr8f5may27fmce9djez3sjz61ra3nld3cza6s6cwi1h2kdezsy04ffqy22nnrls2j2vomre3twxf5fvlultdty0qly0wudu0ergplg9ihoc3hq4ly8r24irwe0tdnxfooklgk2fcuvq1ysbp23gxddf2be4p2qztkfnbeuhhdopsoclc11gfn7brrxb0fuxyvfces0453labraw43lgdm0eh8jfti33wiurp16nqt708vkz0j8kmi80d8e0416rsq3a2xg5us51y8nnag5uevuaax2xf4aunrvjvskm4liy46peszb29edhammjnq03zd9ehpn6gayopq1le9wt4rk5dau7v25zssv3g0227szu29lzktxuze0q3fkw85pagprrw2t8mwtk6zwpjc9xgxczi9o8p93odmbqsfdkk9meqtwo6va6u9wnlrvhjxna206g9n0k0v5t4sjhu7v235vcejzej5jw88fqllbbmh4560lampfdk9iips8s23ipby0010aunojwt6rt433gt2qiwo52cm728t786peowp7w544sat40wk40pugg1sliyl5sg3ce2quvfz00rbysmqacww3e8zsxmg58b3izt5mj46vyzw6qel9lzkwu657d1tljzg7mgrmmh96indjrbhyf2tlmhf4c569e628xfk7bm4s6hp9w8keztez2gnydkt3nr3u4sr4oigws0p0atqt0dhmqsdrijzjbtusyyz2jtazivpuviz5ji2p6l76qkkav4mqhi5nd3u4jxx0cqz64hvp5voqq9814i3msdnjw62m56b761m24fg30gm2atief9w4z7df65stl0k32xbvigvq3shw3pnbxiop4kea3wcudk19ccwo33tjyyzrdjfqzh9pd7uuklr0bss76dncmqllzz4pjn3gj6mkip1jhq3rfe9kx05twgl51qve06pg46xsukdzaw22wejz5kax7911cx3eznarlvvrlqru71ij5yy2cjxnz9g577hm5u1ee7ezuut4ak7e2vefiw7y1iyusraxum0r7o7r91alv2rtuo3o3ywi8lrw1yohd9eer17bu387tun0lrj7gievvvw2ghyrusfnnc8fxsgq2ijrfu6vp2gr3y8idcahkhophjkkh6eylltyey56ir2h29fbmld44ozt7q2y9xtay0aeyl02fe4kt4e0qpmbuee649rx40txi7qah2kl3fxiusx3r3bsqzoojwgc84eixwt7jvbxkk17t7gm1lxw7ffihiiqpq8hlf1des5vy92821nk9mginqi1b11b6lttwpqvan791s9rhuj1vqo0cdngzgz4godesxzw8jc1f6e1zi3c5tmtid5g9sqs543vjbhgw8pujd8pr9tx9xkjobhl2cvg8g0k5ulj5asnydgr2g4igeqh2woxe5ks2de3ngseac7hkiezeal0m8gj4pgl42cxkfpgcvmyro56tisdbc8s45s3imkzxm7hd1b29xh480tyyub1m2akqfuohfyl5diwn51r97zui28zzo2wcrhzs2scxo62jv9bc4jxizdb6whwcufw5dx7dhjls04700kyh7zkl9trsrekzdkneznhf6nt16zjj0pzznxqxrqvwiymdkb7yi4acpfzgu9tcdilx4hj92n4y7bvb5i95g2eidcmo4twujxo9f4gh4l4i90r1utvi9488o4dgn6xid4fwfzh37yyfz0w4wkn4l043or9b75x2xi7b7r9ybqryum40mde5qbjrw0dryf2q3dfhomwrlpfk19hkiwbf8ap4zmz390thni6ubjybaas6wgwt6zp5w2uu1ck3zvq5w16pajl87r24r1kwy4sogwygox6x0bgxw0hi83ng0trhspvvdwy4ilcl7zbu4iv7kefif27ghtcbht3paisy8m9wlx156il92l5yhislj8qr4ca321r63tk3omdmkd390cjq6sl0zwwdsbjsywx2o8z0p6kyi3f9u9jxttk0qgan7o1rfteg3zmref3n7ys013pwu3y4xqeno9w1bftuinby4zrai1m28gryza8fi59n4zfrna8g7en4fdd1kya89801nkhj5ezd3z4i0ve0hadl82f0wbl9gsspqrpmkqb9tyj60zq5qzkd7apc0cfq7dm94msyk2ox2gmiyxjjy9cxi7mbvhp576h1a41zge5ky8ps9e9r8b5r6w9s091inyenw38b7107yra5poksobk5sgrbh1m70oe4tv3rwzmzcn53qo9mowqfejhdi4s84pftn9ee39nswg92cbynzza5xmhgnh9oltabc2e6gdzzeam8leni20xrpjekp05tqkt958z17o9t66i471fwghuruzwjlfd5trug79hn19gwtwwikeqgihngf9xxy7sml12fblbv06wl9xi2jqt47ue0otc7y7cdqdxwghc58neyt5ofazjvwskrjmy81sao587eh5o76czxp2u4ccvs5xb5b6h8mojwiz0kpiqu8a9zuq5t99kdava5z9g6vd6vthf68pse9ocvgtvlv2q1e9gjg8x3gxvdw8604nw9sa09to36d0vczximocexmz6rg9a155hxez4p07e4bejqw5w4uotx5qxdq83spy5lzvhpnrj8yrza1kssggsy1lrelrym81wmkuvd5k0xpyfenzxnn2pgea2qkzien6ymrwrsdxu9zwz7vct0k1rmuewfuqv3muvahelxya0t4ejkay8y8eq9p0s9mkf8souydg2bejz78a1vt31i2nv4c2n4zpehc0oniv4swur4lurton8ypcj5w34x93dezvplz7ik8ua6c6xbzet59r320ghynevrchtmqg3nxdkho7rv1t7ra66i2ujymfrq7dv91nqsq725y1maxr8benqptmvl2xtrlsh5byccq0iv4fjpxv6apf7slonhpn2tl466x20q9a0imhz838eojy1itr0kpore8rg56nj1pt3n7hx2853qqabyyyllie6hhrr8epfk62lbrlni0itavqovau93q8dgzza8hhoc76v18bjk3fktb2mhlql2l9gs4cyvfl76umazmtbqif4cc07n2kl9sc4vrekxed6ggkfjtcx0qy83o7nwa0om61h9dut3lqbkxxhzor70rg3z76k3q87it7ya2rgilvgs3vrq6ycjoo3zbku1oc2udza4nwlmwmpest6jcqa8rhitt676z4kwyncm2xlggw1mkhsqf2i4jf0b1ywxm838ui5tjv6f1qal5lbnd5byzqkb2qmamzz39gpq9zcwlaj57p4sbdpxpljbypukpye3u1ag8x8weo62sceognuxq5jroii3i35raowk8rjbhfv5kml7qc7l53lh7ngx5kkes9xacww8q4vtyrputnt9sa4i5njd50gpgrhmd9hbo40oodv5j2zi4bpl0zl7bkouyxdusc2kaaslnp5paf6fohqb47io34z3vw7qewauuomrym3k1rdkl5jzrxszd492pljuli275vhjfoyjw0lksep00xqwwfxnnlqalm5r602tauvj2z4j3bfvppz70nzy5ff8vbjp0dkn0vtej9f1fkm85edbciwtqgau36qyu8o11mxmtmb5wsz3ol658a6uh8vna9ku14hwjd2ayonpyu257l7aul6wde5sdmi3td12nuse17btcp4cai9fs8v47o353tye6hkgbqug2bb5f35wa42wfxokjv6dc5jbo7zmasj7ufnljkm3p9j3rvvtmr3nmecucxe4shmznleyavp90wj1vuvjced5p5fwth026o9hssdzebt4dsnbd9hrsjeah6tmtu6pdqfgaeul0majrtbb8agxkw21vca0nzbal8jrev9267be7fm2qlu76oq4hofharaca70m0ifjrdex3knypbasmx2thmqq48jcnhuc6wnyj8lzl97b2iy3h5nmkd80azv0291acl37hh845l5thrh73ef0z0r38o3i22gys0p9t92htl0nto2ax6tnpuy6i3ee3c5s4axe7oec03cx7xg9q5yk9r5yl0mkq5qketxvis4ibjrv8vvlrmghdgrr2xq7mfkx37ene39afab0yylnc1xjoq724nuy96o3yj399k42k3u5oeangccn1vghoedo4kh2yusso3dh2m3qcloycuuryylh4aabcn5iigcpdl593sf2knkez59j5nblvbwkfx35vq08blnhv97d0q9628by2unv6ifzj1ymz8s187o2nwlx346r3j56c7bqzc0db709fxj6atzl8m6613xadoosqgrxjjwfv2t8d5n47g01m9fk00x8m19t4y5k8tph3gn2c0y4xz407twh230c0hsjw7zbve42ul4bhus5iei1piywk3i3j70ist3ga98thqaqi84972wn5b6w8l6ulxnljdvk7ybo481x",
                                                                              36
        ));

        byte[] ciphertext = Crypto.rsaEncrypt("awaqwq".getBytes(),
                                              publicKey
        );

        System.out.println(new String(Crypto.rsaDecrypt(ciphertext,
                                                        privateKey
        )));
    }

    public static void ec() throws Exception {
        KeyPair keypair = Crypto.ecKeyPair(521);

        System.out.println(Mathematics.radix(keypair.getPublic()
                                                    .getEncoded(),
                                             36
        ));
        System.out.println(Mathematics.radix(keypair.getPrivate()
                                                    .getEncoded(),
                                             36
        ));

        ECPublicKey publicKey = Crypto.decodeEcPubkey(Mathematics.toBytes("28lbr8j2ese9enb00e8kwy62v8zky2peyojoepyntitqufu4o77qpndw1y0k8kjwdzb9way1r6fkhqlaznid8udyx3azxsc2744ly1iyfifytsfq4qqfsvnmrd64r9jkizuzr3e9i4w2oslxphcxjvvyknb3ish8sx70mdvxsok7z27mh7ijh3clmb",
                                                                          36
        ));
        ECPrivateKey privateKey = Crypto.decodeEcPrikey(Mathematics.toBytes("duumyfr69utbni86zki6mhah52785oqn0lfhh08qcr7t0cht01jn8smbvu0isu4jncsmwww82sr4wdxay221d9ctaahszwyxsokafl8kpj9gqfnjps9q8gnux16uqr9yawx21aodp85vznc420cje4897p04te9cklsdn1j97mum1v7dhx3raodldd9b4warajakeilzpfryxqn564wcd3u7cll323vkj35etr8wbm3kbku83sfmmkhsyciz8nla78hvm292iohvbuoyl5ejqdseoycoxk5dqnefi8e9cdbn",
                                                                            36
        ));

        byte[] ciphertext = Crypto.ecEncrypt("awaqwq".getBytes(),
                                             publicKey
        );

        System.out.println(new String(Crypto.ecDecrypt(ciphertext,
                                                       privateKey
        )));
    }

    public static void ecSign() throws Exception {
        ECPublicKey publicKey = Crypto.decodeEcPubkey(Mathematics.toBytes("28lbr8j2ese9enb00e8kwy62v8zky2peyojoepyntitqufu4o77qpndw1y0k8kjwdzb9way1r6fkhqlaznid8udyx3azxsc2744ly1iyfifytsfq4qqfsvnmrd64r9jkizuzr3e9i4w2oslxphcxjvvyknb3ish8sx70mdvxsok7z27mh7ijh3clmb",
                                                                          36
        ));
        ECPrivateKey privateKey = Crypto.decodeEcPrikey(Mathematics.toBytes("duumyfr69utbni86zki6mhah52785oqn0lfhh08qcr7t0cht01jn8smbvu0isu4jncsmwww82sr4wdxay221d9ctaahszwyxsokafl8kpj9gqfnjps9q8gnux16uqr9yawx21aodp85vznc420cje4897p04te9cklsdn1j97mum1v7dhx3raodldd9b4warajakeilzpfryxqn564wcd3u7cll323vkj35etr8wbm3kbku83sfmmkhsyciz8nla78hvm292iohvbuoyl5ejqdseoycoxk5dqnefi8e9cdbn",
                                                                            36
        ));

        byte[] plaintext = "awaqwq".getBytes();

        byte[] ciphertext = Crypto.ecEncrypt(plaintext,
                                             publicKey
        );

        System.out.println(new String(Crypto.ecDecrypt(ciphertext,
                                                       privateKey
        )));

        System.out.println(new String(ciphertext));

        System.out.println(Crypto.ecVerify(plaintext,
                                           Crypto.ecSign(plaintext,
                                                         privateKey
                                           ),
                                           publicKey
        ));
    }

    public static void rsaSign() throws Exception {
        RSAPublicKey publicKey = Crypto.decodeRsaPubkey(Mathematics.toBytes("nz02b2aac6hyht2sw5ry3hiyfi3h2xlgruxvpuolw2cg4rvut0mog6y0y3isvvy05txuvjbra02e1i6g8rwdtzvyewodtpik1rekl65sr37kw6748qvr4dwc5ptv53hbhtx4ray6szcj89xwmotbuxoy9sp2wqgqyymcgc1k7rbfrvvo9a8sad2pgll80du4zy8julo8rkijicxxx40eq61pinmp7tp1t75uns9todd8tu4jx6opvoflfno6qxhaccerfmo6u4cv9ajnialmv2lmhcruu6nmhfrhwskbp3h872j68xatl8r20r2h55eg5s7ncovtp4lukblnj539sfo9lfgzlfdwq81iokuwqoe0s29q1ff0dtublxkg062itejvycyfo7ft5le25elbkyjietesixq4t84ztwxjyluf4jvoten2acdv0qrv67fxixmumsockgbzl1988yjfrlh4pfqky6ovj6u2mv0dqusfgc2jbxon2gh3z1nd75f2n4e87mxu1pwk3gih1szf5ahjlkxwppa1a2ofj3q749x7h1c3rfo6k4z9touh44fzjw9uqdhvnq3euwnajiv7qexsww2k3aftkp8gkekqcozj5batdx834rqmihqykrv6u38m85lshxgotghuk9pbkswbl5eui6awbilaf90zc6k9601ms33ac19bz1sbcmqf1hdf4qwwcg7alt6twr5wixb10j6hrc9q14qsv8grtr6r9fj2b84o0m54fdyo2u5a62jcj2nin6bwyv9gchi4lc40nmqklgz80k108925eg0qn0bxybswnkdd59uea0ubs6x81mxxntizla58j5z99y8ehe6rwb1ok3mvadd28b8mjmnz7l2tl419fr8ohvuan6yzfh53gi7naqzle3nwo098e6ff5nb135msreqagkyyphxsgc029lmq5by69x2kkvrfyzdh13zivcn4ivre4pyto8lb9brkkwusmsrpakvh436kovxz1jzo5jpliazu0erp0wb9o6mlod6f639hghmnj218l3lfkuauxakn4kaltyoma2b6ws7jlcjbvrr451x875bb1gvp96lh05rvvzzkd3j9g28eo023l32aixwhvalggl2g30e7gdzn1k38q909p5sdc4eeh70oxllc7qjzbt6ee1c6pb7utp4ovmx3f4vpidr0qcjydgvo65zewb4bu4i1yldfmlj5t0ptmjxkmz8vkr7ouryewl0kef1kxrqehqjtgwesp8901jxd55jmqx4ffy3u4i4rar4wqdmuzo6m8r6yph6ld9icrqnr6c3pyzjw9db7ipnkw930kf838mgz4nb3sz34b9if0ixktaycpn3dr7jstwdkk9e50k5zofpkoaj1c93rdufeb8yy09lzajafung1gu1oe271evbdnqr0pl8g8ok1pxpp4ksnsn44zuu4qakg11osc5glcwsbdh5pes2teek0xgiuieoznqpgeuq4rthl6se1idv0zupru0j1k4zdlshjs8spc73m8jwryt69ogcfzkcctorjxlxwqgmmtq4ps358o0djmspch28st8af2h5l59nye8xrcqmblzt137g7yx4s0splc1zlz36y730o2rl",
                                                                            36
        ));
        RSAPrivateKey privateKey = Crypto.decodeRsaPrikey(Mathematics.toBytes("mtzyvm2ov17ukv59pt550bk7hr8ocql88rd41kpphx91fmq5d1nq1wa8hu5wawgaonpv594gkfhe43pgfnpm6ad2jmi88ma1r2e3662ng3v0kf0anr18uza3keu8lvp5jqwmrd6scvexb0difej7dszrh9td3n59w28x0mqddfm2or7tx4u6xm8lw2xndm5lgo2jmure5am7zatgoi3rqslxyuz566y74hlbweq6hgraj0ccsf8mnyh9y20b7rs8y4amdkyp0r8trhr2fqybcmq5g2rrwcgfd25ei4z07jrmh76z5l442v0batu8yrguds8eljsticeujftagtgqvdg5x19syj071j541s7j3in6eixgp5j43h3a8am2gyw74xja56t9hygcx7p6pqgv3jhgqm7jezauxyj1whdch4dkhzkkh4bojtl5fuc5adxfbl0dp6wopunvfokmbsxrm3ceo5cdmosk5pi372ed651l2hgrxe1plb1x7op6x3qp73o686jtyutk9n29dsb8bprm0xgp0szjobj6cl4lgv29jw5ezeaszms8v6hs7en80vkcgxh58fsv0tsb1o1clrylwxjmhvuikfnxudjw8rliyu4tnto4ach540tlidibjnlqa32kclke4k0rwsxq65hq6bd8a0lpc6v3c9d7wfcyilrb10qu9o7tbzx290trbb61o8jl8sqvb10kynpawti3ztftpfsnjk5sfnmxfpmedjbiz1kfchuhqo49wxttby4yk5yz8je3ngz1a0vdx1jyrabidbtv21xcz9do8t066auqty4xxzgaz2ruzlpajjoconnfbam87baepvivco5us8y8wh2u6iae9z1zkrcol22x123vept5kwhzw6awyfdlzjqcwdkffaqils89o0xxuzmhpiyb7nznhibrw305w7yz3htxfpfyt35fv2hnoy545z3keb2799c92ai2eeuppll7xz0ksncitc90izseufrjmrjmngh1agnnpz5gqg4c25hbrdy8iwdblw3qmzaks8jv6iuefyfacrf5qgtdqhals6eor0xw9klygp7zj9v7hbds8co3b26b0bstphvgerhyt3dlg6asgr4zh6ma0rwdm4rurg3d33cdvp03q64vovwpzpnup9gqa8crt6f8crvtm55hg1vthvuhp8f8nlt94694rhkqvbrl0n6jbdz2c9h2opc2jsz21j8bgdkfg6qdv70c2hp8pmk4i92so5t67q7utyqhjxp3lme1egqmwhe8lx0dx5lds5ic7xxswrjxqpo2v6w5drdmcbj0hue5trrh5cnomz5ndey043t9guh5zry4yqvl55pelx9j8qn49qyf1cfadim9dl1d2s08l3tbxqdi3srau35y521cf3qi4ldhr1vdbk54wl5k2ejhtyzit6zrsr21c09yffkdtnwjiohygkh7frriif62ikwqxw83dpuu5hz1pjrrvd45m9g08q69phiw7cx9w26efxvkeaprz6etbf95xjrhw6w20fdt301ahcipbmnemow40p9uykyc5eax2484seqg4q1es9gfhckgyqyxgnh7aupmf3va9zw29gtuby5sk408no5jznysiz1491hhkpg425lc7vf2yvd3vz65g32dz1dki6v0j3gk6wk3i1mscsc4473vebfdlkbkab3fqbwsgp40pwor1n5wxuzfk019wv4bz4magybkrbb74b3ccpmdjbu21481por6ydow3t96duwl1jp9bpjdaapzxqe7o20fyr6yyvqfqgugc86tykvovlgerwd5dcvlxcpiym2vlq4gtayjga85embescjiovcfz2fi0e0y1tknwqg6v7u82c5jaiu2hlf9zfh9yvd4henus6iv3914anpludz2n3uyxzrej3xonxzgh1zb1trehh2dvw9yzf2hjx5gsa9iwgzbkeeftzgua598t3z8hlkhhjxttea6ehc6rr05u17re3d6l4rq619c6hfv0dwk0adjqvkpi00cjgtk4o5ng31etc7azrgewia7pho4g9a0safshki52leltbqhbq7syc301wald133z5bwotxux9yoh9ox7p9kqv44moa4em3w0mcoijzbgyc2mb4wnkvcvo3p589ardylkd51ia0hr7nry9hdodiw6rld4c1sfxisa915hjt58q4yvg6ygdz0pcwc3649j4vzag43ujp89agsbx3xlxvw88tqp7sfufykjzp99cldwmbxnlnl8goxp8mnzpsjgw0kpyo217q9q78gkrqo0uz2g68k55iuvx3rmdxuo72iv15cas7dfcgvdhiz04j03b17zxouq1avl10e06j82c8zbk8e4cu3s29d74dpanlkny75c5k25oue0b9ggxnn1honhhu5wmh5qyh0hsdtpb6qwhmfwb56d1yffbtlaglo8c4x7dvswwymatp29zeo9nmhy55nm8avqebfokoiiwqwl9mt2gcsz618cwook9a777092gsd0m8jri3w3dp2n214wxj5ndlk6360au2dqdritgphrotxch9m6vfoxfmhu1vqf7lmtz9lbsgagrzefqy7puz9l8b9urultxwugy5z7o285uga3uv4djhmx9hqs7mvu3g3mo73i4mrdt1c2517weimwnukcmx6lpes8kuz9uuswxp8bpzu4plzcsnst0l75o9gkztn94me3zr8f5may27fmce9djez3sjz61ra3nld3cza6s6cwi1h2kdezsy04ffqy22nnrls2j2vomre3twxf5fvlultdty0qly0wudu0ergplg9ihoc3hq4ly8r24irwe0tdnxfooklgk2fcuvq1ysbp23gxddf2be4p2qztkfnbeuhhdopsoclc11gfn7brrxb0fuxyvfces0453labraw43lgdm0eh8jfti33wiurp16nqt708vkz0j8kmi80d8e0416rsq3a2xg5us51y8nnag5uevuaax2xf4aunrvjvskm4liy46peszb29edhammjnq03zd9ehpn6gayopq1le9wt4rk5dau7v25zssv3g0227szu29lzktxuze0q3fkw85pagprrw2t8mwtk6zwpjc9xgxczi9o8p93odmbqsfdkk9meqtwo6va6u9wnlrvhjxna206g9n0k0v5t4sjhu7v235vcejzej5jw88fqllbbmh4560lampfdk9iips8s23ipby0010aunojwt6rt433gt2qiwo52cm728t786peowp7w544sat40wk40pugg1sliyl5sg3ce2quvfz00rbysmqacww3e8zsxmg58b3izt5mj46vyzw6qel9lzkwu657d1tljzg7mgrmmh96indjrbhyf2tlmhf4c569e628xfk7bm4s6hp9w8keztez2gnydkt3nr3u4sr4oigws0p0atqt0dhmqsdrijzjbtusyyz2jtazivpuviz5ji2p6l76qkkav4mqhi5nd3u4jxx0cqz64hvp5voqq9814i3msdnjw62m56b761m24fg30gm2atief9w4z7df65stl0k32xbvigvq3shw3pnbxiop4kea3wcudk19ccwo33tjyyzrdjfqzh9pd7uuklr0bss76dncmqllzz4pjn3gj6mkip1jhq3rfe9kx05twgl51qve06pg46xsukdzaw22wejz5kax7911cx3eznarlvvrlqru71ij5yy2cjxnz9g577hm5u1ee7ezuut4ak7e2vefiw7y1iyusraxum0r7o7r91alv2rtuo3o3ywi8lrw1yohd9eer17bu387tun0lrj7gievvvw2ghyrusfnnc8fxsgq2ijrfu6vp2gr3y8idcahkhophjkkh6eylltyey56ir2h29fbmld44ozt7q2y9xtay0aeyl02fe4kt4e0qpmbuee649rx40txi7qah2kl3fxiusx3r3bsqzoojwgc84eixwt7jvbxkk17t7gm1lxw7ffihiiqpq8hlf1des5vy92821nk9mginqi1b11b6lttwpqvan791s9rhuj1vqo0cdngzgz4godesxzw8jc1f6e1zi3c5tmtid5g9sqs543vjbhgw8pujd8pr9tx9xkjobhl2cvg8g0k5ulj5asnydgr2g4igeqh2woxe5ks2de3ngseac7hkiezeal0m8gj4pgl42cxkfpgcvmyro56tisdbc8s45s3imkzxm7hd1b29xh480tyyub1m2akqfuohfyl5diwn51r97zui28zzo2wcrhzs2scxo62jv9bc4jxizdb6whwcufw5dx7dhjls04700kyh7zkl9trsrekzdkneznhf6nt16zjj0pzznxqxrqvwiymdkb7yi4acpfzgu9tcdilx4hj92n4y7bvb5i95g2eidcmo4twujxo9f4gh4l4i90r1utvi9488o4dgn6xid4fwfzh37yyfz0w4wkn4l043or9b75x2xi7b7r9ybqryum40mde5qbjrw0dryf2q3dfhomwrlpfk19hkiwbf8ap4zmz390thni6ubjybaas6wgwt6zp5w2uu1ck3zvq5w16pajl87r24r1kwy4sogwygox6x0bgxw0hi83ng0trhspvvdwy4ilcl7zbu4iv7kefif27ghtcbht3paisy8m9wlx156il92l5yhislj8qr4ca321r63tk3omdmkd390cjq6sl0zwwdsbjsywx2o8z0p6kyi3f9u9jxttk0qgan7o1rfteg3zmref3n7ys013pwu3y4xqeno9w1bftuinby4zrai1m28gryza8fi59n4zfrna8g7en4fdd1kya89801nkhj5ezd3z4i0ve0hadl82f0wbl9gsspqrpmkqb9tyj60zq5qzkd7apc0cfq7dm94msyk2ox2gmiyxjjy9cxi7mbvhp576h1a41zge5ky8ps9e9r8b5r6w9s091inyenw38b7107yra5poksobk5sgrbh1m70oe4tv3rwzmzcn53qo9mowqfejhdi4s84pftn9ee39nswg92cbynzza5xmhgnh9oltabc2e6gdzzeam8leni20xrpjekp05tqkt958z17o9t66i471fwghuruzwjlfd5trug79hn19gwtwwikeqgihngf9xxy7sml12fblbv06wl9xi2jqt47ue0otc7y7cdqdxwghc58neyt5ofazjvwskrjmy81sao587eh5o76czxp2u4ccvs5xb5b6h8mojwiz0kpiqu8a9zuq5t99kdava5z9g6vd6vthf68pse9ocvgtvlv2q1e9gjg8x3gxvdw8604nw9sa09to36d0vczximocexmz6rg9a155hxez4p07e4bejqw5w4uotx5qxdq83spy5lzvhpnrj8yrza1kssggsy1lrelrym81wmkuvd5k0xpyfenzxnn2pgea2qkzien6ymrwrsdxu9zwz7vct0k1rmuewfuqv3muvahelxya0t4ejkay8y8eq9p0s9mkf8souydg2bejz78a1vt31i2nv4c2n4zpehc0oniv4swur4lurton8ypcj5w34x93dezvplz7ik8ua6c6xbzet59r320ghynevrchtmqg3nxdkho7rv1t7ra66i2ujymfrq7dv91nqsq725y1maxr8benqptmvl2xtrlsh5byccq0iv4fjpxv6apf7slonhpn2tl466x20q9a0imhz838eojy1itr0kpore8rg56nj1pt3n7hx2853qqabyyyllie6hhrr8epfk62lbrlni0itavqovau93q8dgzza8hhoc76v18bjk3fktb2mhlql2l9gs4cyvfl76umazmtbqif4cc07n2kl9sc4vrekxed6ggkfjtcx0qy83o7nwa0om61h9dut3lqbkxxhzor70rg3z76k3q87it7ya2rgilvgs3vrq6ycjoo3zbku1oc2udza4nwlmwmpest6jcqa8rhitt676z4kwyncm2xlggw1mkhsqf2i4jf0b1ywxm838ui5tjv6f1qal5lbnd5byzqkb2qmamzz39gpq9zcwlaj57p4sbdpxpljbypukpye3u1ag8x8weo62sceognuxq5jroii3i35raowk8rjbhfv5kml7qc7l53lh7ngx5kkes9xacww8q4vtyrputnt9sa4i5njd50gpgrhmd9hbo40oodv5j2zi4bpl0zl7bkouyxdusc2kaaslnp5paf6fohqb47io34z3vw7qewauuomrym3k1rdkl5jzrxszd492pljuli275vhjfoyjw0lksep00xqwwfxnnlqalm5r602tauvj2z4j3bfvppz70nzy5ff8vbjp0dkn0vtej9f1fkm85edbciwtqgau36qyu8o11mxmtmb5wsz3ol658a6uh8vna9ku14hwjd2ayonpyu257l7aul6wde5sdmi3td12nuse17btcp4cai9fs8v47o353tye6hkgbqug2bb5f35wa42wfxokjv6dc5jbo7zmasj7ufnljkm3p9j3rvvtmr3nmecucxe4shmznleyavp90wj1vuvjced5p5fwth026o9hssdzebt4dsnbd9hrsjeah6tmtu6pdqfgaeul0majrtbb8agxkw21vca0nzbal8jrev9267be7fm2qlu76oq4hofharaca70m0ifjrdex3knypbasmx2thmqq48jcnhuc6wnyj8lzl97b2iy3h5nmkd80azv0291acl37hh845l5thrh73ef0z0r38o3i22gys0p9t92htl0nto2ax6tnpuy6i3ee3c5s4axe7oec03cx7xg9q5yk9r5yl0mkq5qketxvis4ibjrv8vvlrmghdgrr2xq7mfkx37ene39afab0yylnc1xjoq724nuy96o3yj399k42k3u5oeangccn1vghoedo4kh2yusso3dh2m3qcloycuuryylh4aabcn5iigcpdl593sf2knkez59j5nblvbwkfx35vq08blnhv97d0q9628by2unv6ifzj1ymz8s187o2nwlx346r3j56c7bqzc0db709fxj6atzl8m6613xadoosqgrxjjwfv2t8d5n47g01m9fk00x8m19t4y5k8tph3gn2c0y4xz407twh230c0hsjw7zbve42ul4bhus5iei1piywk3i3j70ist3ga98thqaqi84972wn5b6w8l6ulxnljdvk7ybo481x",
                                                                              36
        ));

        byte[] plaintext = "awaqwq".getBytes();

        byte[] ciphertext = Crypto.rsaEncrypt(plaintext,
                                              publicKey
        );
        System.out.println(new String(Crypto.rsaDecrypt(ciphertext,
                                                        privateKey
        )));

        System.out.println(new String(ciphertext));

        System.out.println(Crypto.rsaVerify(plaintext,
                                            Crypto.rsaSign(plaintext,
                                                           privateKey
                                            ),
                                            publicKey
        ));
    }
}
