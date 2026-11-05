package com.jalanislam.app.data.quran

object CuratedQuranVerses {

    fun getVerses(surahNumber: Int, langCode: String = "id"): List<AyahItem> {
        val lang = langCode.lowercase()
        return when (surahNumber) {
            1 -> getAlFatihah(lang)
            112 -> getAlIkhlas(lang)
            113 -> getAlFalaq(lang)
            114 -> getAnNas(lang)
            108 -> getAlKautsar(lang)
            103 -> getAlAsr(lang)
            67 -> getAlMulk(lang)
            else -> emptyList()
        }
    }

    private fun getAlFatihah(lang: String): List<AyahItem> {
        val translations = when (lang) {
            "en" -> listOf(
                "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
                "[All] praise is [due] to Allah, Lord of the worlds -",
                "The Entirely Merciful, the Especially Merciful,",
                "Sovereign of the Day of Recompense.",
                "It is You we worship and You we ask for help.",
                "Guide us to the straight path -",
                "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray."
            )
            "ms" -> listOf(
                "Dengan nama Allah, Yang Maha Pemurah, lagi Maha Mengasihani.",
                "Segala puji tertentu bagi Allah, Tuhan sekalian alam.",
                "Yang Maha Pemurah, lagi Maha Mengasihani.",
                "Pemerintah hari pembalasan (hari Akhirat).",
                "Engkaulah sahaja yang kami sembah dan kepada Engkaulah sahaja kami memohon pertolongan.",
                "Tunjuklah kami jalan yang lurus.",
                "Iaitu jalan orang-orang yang Engkau telah kurniakan nikmat kepada mereka, bukan (jalan) orang-orang yang dimurkai dan bukan (pula jalan) orang-orang yang sesat."
            )
            "ar" -> listOf(
                "باسم الله الرحمن الرحيم",
                "الحمد لله رب العالمين",
                "الرحمن الرحيم",
                "مالك يوم الدين",
                "إياك نعبد وإياك نستعين",
                "اهدنا الصراط المستقيم",
                "صراط الذين أنعمت عليهم غير المغضوب عليهم ولا الضالين"
            )
            "th" -> listOf(
                "ด้วยพระนามแห่งอัลลอฮฺ ผู้ทรงกรุณาปรานี ผู้ทรงเมตตาเสมอ",
                "การสรรเสริญทั้งมวลเป็นสิทธิ์ของอัลลอฮฺ พระผู้อภิบาลแห่งสากลโลก",
                "ผู้ทรงกรุณาปรานี ผู้ทรงเมตตาเสมอ",
                "ผู้ทรงอภิสิทธิ์แห่งวันแห่งการตอบแทน",
                "พระองค์เท่านั้นที่พวกข้าพระองค์เคารพภักดี และพระองค์เท่านั้นที่พวกข้าพระองค์ขอความช่วยเหลือ",
                "ขอพระองค์ทรงนำพวกข้าพระองค์สู่หนทางอันเที่ยงตรง",
                "หนทางของบรรดาผู้ที่พระองค์ได้ทรงโปรดปรานแก่พวกเขา มิใช่หนทางของผู้ที่ถูกกริ้ว และมิใช่หนทางของผู้ที่หลงผิด"
            )
            "ja" -> listOf(
                "慈悲あまねく慈愛深きアッラーの御名において。",
                "万有の主、アッラーにこそ凡ての称賛あれ、",
                "慈悲あまねく慈愛深き御方、",
                "審判の日の主宰者に。",
                "わたしたちはあなたにのみ信仰につかえ、あなたにのみ御助けを請い願います。",
                "わたしたちを正しい道に導いて下さい、",
                "あなたが恩恵を施された者たちの道に、あなたの怒りを被った者や、迷い去った者たちの道ではなく。"
            )
            "hi" -> listOf(
                "अल्लाह के नाम से जो बड़ा कृपाशील और अत्यंत दयावान है।",
                "सब तारीफ़ अल्लाह ही के लिए है जो सारे संसार का रब है।",
                "बड़ा कृपाशील और अत्यंत दयावान है।",
                "न्याय के दिन का मालिक है।",
                "हम तेरी ही इबादत करते हैं और तुझ ही से मदद माँगते हैं।",
                "हमें सीधे मार्ग पर चला।",
                "उन लोगों का मार्ग जिन पर तूने कृपा की, न कि उनका मार्ग जिन पर तेरा प्रकोप हुआ और न बहके हुओं का।"
            )
            "bn" -> listOf(
                "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।",
                "সমস্ত প্রশংসা আল্লাহর জন্য, যিনি সকল সৃষ্টির পালনকর্তা।",
                "যিনি পরম করুণাময় ও অতি দয়ালু।",
                "বিচার দিবসের মালিক।",
                "আমরা একমাত্র তোমারই ইবাদত করি এবং শুধুমাত্র তোমারই সাহায্য প্রার্থনা করি।",
                "আমাদের সরল পথ দেখাও,",
                "সে সমস্ত লোকের পথ, যাদেরকে তুমি নেয়ামত দান করেছ। তাদের পথ নয়, যাদের প্রতি তোমার গজব নাযিল হয়েছে এবং যারা পথভ্রষ্ট হয়েছে।"
            )
            "ur" -> listOf(
                "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے۔",
                "سب تعریفیں اللہ کے لیے ہیں جو تمام جہانوں کا پالنے والا ہے۔",
                "بڑا مہربان نہایت رحم کرنے والا ہے۔",
                "روزِ جزا کا مالک ہے۔",
                "ہم تیری ہی عبادت کرتے ہیں اور تجھ ہی سے مدد مانگتے ہیں۔",
                "ہمیں سیدھا راستہ دکھا۔",
                "ان لوگوں کا راستہ جن پر تو نے انعام فرمایا، نہ کہ ان کا جن پر غضب ہوا اور نہ گمراہوں کا۔"
            )
            "zh" -> listOf(
                "奉至仁至慈的真主之名。",
                "一切赞颂，全归真主，全世界的主，",
                "至仁至慈的主，",
                "报应日的主。",
                "我们只崇拜你，只求你佑助。",
                "求你引导我们上正路，",
                "你所佑助者的路，不是受谴怒者的路，也不是迷误者的路。"
            )
            "tr" -> listOf(
                "Rahmân ve Rahîm olan Allah'ın adıyla.",
                "Hamd, âlemlerin Rabbi olan Allah'a mahsustur.",
                "O, Rahmân'dır, Rahîm'dir.",
                "Din gününün (hesap gününün) sahibidir.",
                "(Rabbimiz!) Yalnız sana kulluk eder ve yalnız senden yardım dileriz.",
                "Bizi doğru yola ilet,",
                "Kendilerine lütufta bulunduğun kimselerin yoluna; gazaba uğramışların ve sapmışların yoluna değil."
            )
            "fr" -> listOf(
                "Au nom d'Allah, le Tout Miséricordieux, le Très Miséricordieux.",
                "Louange à Allah, Seigneur de l'univers.",
                "Le Tout Miséricordieux, le Très Miséricordieux,",
                "Maître du Jour de la rétribution.",
                "C'est Toi [Seul] que nous adorons, et c'est Toi [Seul] dont nous implorons secours.",
                "Guide-nous dans le droit chemin,",
                "le chemin de ceux que Tu as comblés de faveurs, non pas de ceux qui ont encouru Ta colère, ni des égarés."
            )
            "de" -> listOf(
                "Im Namen Allahs, des Allerbarmers, des Barmherzigen.",
                "Alles Lob gebührt Allah, dem Herrn der Welten,",
                "dem Allerbarmer, dem Barmherzigen,",
                "dem Herrscher am Tage des Gerichts.",
                "Dir allein dienen wir, und Dich allein bitten wir um Hilfe.",
                "Führe uns den geraden Weg,",
                "den Weg derer, denen Du Gnade erwiesen hast, nicht derer, die Zorn erregt haben, und nicht der Irregehenden."
            )
            else -> listOf(
                "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.",
                "Segala puji bagi Allah, Tuhan semesta alam.",
                "Maha Pengasih, Maha Penyayang.",
                "Pemilik hari pembalasan.",
                "Hanya kepada Engkaulah kami menyembah dan hanya kepada Engkaulah kami mohon pertolongan.",
                "Tunjukilah kami jalan yang lurus,",
                "(yaitu) jalan orang-orang yang telah Engkau beri nikmat kepadanya; bukan (jalan) mereka yang dimurkai, dan bukan (pula jalan) mereka yang sesat."
            )
        }

        return listOf(
            AyahItem(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "Bismillahir-rahmanir-rahim", translations[0], "https://everyayah.com/data/Alafasy_128kbps/001001.mp3"),
            AyahItem(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "Al-hamdu lillahi rabbil-'alamin", translations[1], "https://everyayah.com/data/Alafasy_128kbps/001002.mp3"),
            AyahItem(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "Ar-rahmanir-rahim", translations[2], "https://everyayah.com/data/Alafasy_128kbps/001003.mp3"),
            AyahItem(1, 4, "مَالِكِ يَوْمِ الدِّينِ", "Maliki yaumid-din", translations[3], "https://everyayah.com/data/Alafasy_128kbps/001004.mp3"),
            AyahItem(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "Iyyaka na'budu wa iyyaka nasta'in", translations[4], "https://everyayah.com/data/Alafasy_128kbps/001005.mp3"),
            AyahItem(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Ihdinas-siratal-mustaqim", translations[5], "https://everyayah.com/data/Alafasy_128kbps/001006.mp3"),
            AyahItem(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "Siratalladhina an'amta 'alaihim gairil-magdubi 'alaihim wa lad-dallin", translations[6], "https://everyayah.com/data/Alafasy_128kbps/001007.mp3")
        )
    }

    private fun getAlIkhlas(lang: String): List<AyahItem> {
        val translations = when (lang) {
            "en" -> listOf(
                "Say, \"He is Allah, [who is] One,",
                "Allah, the Eternal Refuge.",
                "He neither begets nor is born,",
                "Nor is there to Him any equivalent.\""
            )
            "ms" -> listOf(
                "Katakanlah (wahai Muhammad): \"Tuhanku ialah Allah Yang Maha Esa.\"",
                "\"Allah Yang menjadi tumpuan sekalian makhluk untuk memohon sebarang hajat.\"",
                "\"Ia tiada beranak, dan Ia pula tidak diperanakkan.\"",
                "\"Dan tidak ada sesiapapun yang serupa dengan-Nya.\""
            )
            "ar" -> listOf(
                "قل هو الله أحد",
                "الله الصمد",
                "لم يلد ولم يولد",
                "ولم يكن له كفوا أحد"
            )
            "th" -> listOf(
                "จงกล่าวเถิด (มุฮัมมัด) \"พระองค์คืออัลลอฮฺผู้ทรงเอกะ\"",
                "\"อัลลอฮฺผู้ทรงเป็นที่พึ่งอันนิรันดร์\"",
                "\"พระองค์ไม่ประสูติ และไม่ทรงถูกประสูติ\"",
                "\"และไม่มีผู้ใดเสมอเหมือนพระองค์\""
            )
            "ja" -> listOf(
                "言ってやるがいい。「かれはアッラー、唯一なる御方であられる。",
                "アッラーは、自存され、凡ての者が頼る御方。",
                "かれは生まず、生れず、",
                "かれに比べ得る何ものもない。」"
            )
            "hi" -> listOf(
                "कहो: \"वह अल्लाह एक है,",
                "अल्लाह निरपेक्ष (सबका सहारा) है,",
                "न उसकी कोई सन्तान है और न वह किसी की सन्तान,",
                "और न कोई उसका समकक्ष है।\""
            )
            "bn" -> listOf(
                "বলুন, তিনিই আল্লাহ, এক-অদ্বিতীয়,",
                "আল্লাহ কারও মুখাপেক্ষী নন, সকলেই তাঁর মুখাপেক্ষী,",
                "তিনি কাউকে জন্ম দেননি এবং তাঁকেও কেউ জন্ম দেয়নি,",
                "এবং তাঁর সমতুল্য কেউই নেই।"
            )
            "ur" -> listOf(
                "کہہ دیجئے: وہ اللہ ایک ہے،",
                "اللہ بے نیاز ہے،",
                "نہ اس کی کوئی اولاد ہے اور نہ وہ کسی کی اولاد ہے،",
                "اور کوئی اس کا ہمسر نہیں ہے۔"
            )
            "zh" -> listOf(
                "你说：他是真主，是独一的主；",
                "真主是万物所仰赖的；",
                "他没有生产，也没有被生产；",
                "没有任何物可以做他的匹敌。"
            )
            "tr" -> listOf(
                "De ki: O Allah tektir.",
                "Allah Samed'dir (her şey O'na muhtaçtır, O hiçbir şeye muhtaç değildir).",
                "O, doğurmamış ve doğmamıştır.",
                "Ve hiçbir şey O'na denk değildir."
            )
            "fr" -> listOf(
                "Dis: \"Il est Allah, Unique.",
                "Allah, Le Seul à être imploré pour ce que nous désirons.",
                "Il n'a jamais engendré, n'a pas été engendré non plus.",
                "Et nul n'est égal à Lui.\""
            )
            "de" -> listOf(
                "Sprich: Er ist Allah, ein Einziger,",
                "Allah, der Absolute (ewig Unabhängige).",
                "Er zeugt nicht und ist nicht gezeugt worden,",
                "und niemand ist Ihm ebenbürtig."
            )
            else -> listOf(
                "Katakanlah (Muhammad): \"Dialah Allah, Yang Maha Esa.\"",
                "Allah tempat meminta segala sesuatu.",
                "(Allah) tidak beranak dan tidak pula diperanakkan,",
                "dan tidak ada sesuatu yang setara dengan Dia."
            )
        }

        return listOf(
            AyahItem(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Qul huwallahu ahad", translations[0], "https://everyayah.com/data/Alafasy_128kbps/112001.mp3"),
            AyahItem(112, 2, "اللَّهُ الصَّمَدُ", "Allahus-samad", translations[1], "https://everyayah.com/data/Alafasy_128kbps/112002.mp3"),
            AyahItem(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "Lam yalid wa lam yulad", translations[2], "https://everyayah.com/data/Alafasy_128kbps/112003.mp3"),
            AyahItem(112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Wa lam yakun lahu kufuwan ahad", translations[3], "https://everyayah.com/data/Alafasy_128kbps/112004.mp3")
        )
    }

    private fun getAlFalaq(lang: String): List<AyahItem> {
        val translations = when (lang) {
            "en" -> listOf(
                "Say, \"I seek refuge in the Lord of daybreak",
                "From the evil of that which He created",
                "And from the evil of darkness when it settles",
                "And from the evil of the blowers in knots",
                "And from the evil of an envier when he envies.\""
            )
            "ms" -> listOf(
                "Katakanlah (wahai Muhammad): \"Aku berlindung kepada Tuhan yang menciptakan fajar,",
                "\"Dari kejahatan makhluk-makhluk yang Ia ciptakan;",
                "\"Dan dari kejahatan malam apabila ia gelap gelita;",
                "\"Dan dari kejahatan tukang-tukang sihir yang meniup pada simpulan-simpulan (ikatan);",
                "\"Dan dari kejahatan orang yang dengki apabila ia melakukan dengkinya.\""
            )
            "ar" -> listOf(
                "قل أعوذ برب الفلق",
                "من شر ما خلق",
                "ومن شر غاسق إذا وقب",
                "ومن شر النفاثات في العقد",
                "ومن شر حاسد إذا حسد"
            )
            "th" -> listOf(
                "จงกล่าวเถิด \"ข้าพระองค์ขอความคุ้มครองต่อพระผู้อภิบาลแห่งรุ่งอรุณ",
                "ให้พ้นจากความชั่วร้ายแห่งสิ่งที่พระองค์ได้ทรงสร้างไว้",
                "และจากความชั่วร้ายแห่งความมืดมิดเมื่อมันแผ่คลุม",
                "และจากความชั่วร้ายของบรรดาผู้เป่าบนปมเงื่อน",
                "และจากความชั่วร้ายของผู้อิจฉาเมื่อเขาอิจฉา\""
            )
            "ja" -> listOf(
                "言ってやるがいい。「わたしは黎明の主の加護を乞う。",
                "かれが創造されたものの悪より、",
                "忍び寄る夜の闇の悪より、",
                "結び目に息を吹きかける魔女の悪より、",
                "また嫉妬する者の嫉妬の悪より。」"
            )
            "hi" -> listOf(
                "कहो: \"मैं सुबह के रब की शरण माँगता हूँ,",
                "हर उस चीज़ की बुराई से जो उसने पैदा की,",
                "और रात के अँधेरे की बुराई से जब वह छा जाए,",
                "और गाँठों पर फूँकने वालियों की बुराई से,",
                "और ईर्ष्यालु की बुराई से जब वह ईर्ष्या करे।\""
            )
            "bn" -> listOf(
                "বলুন, আমি আশ্রয় গ্রহণ করছি প্রভাতের পালনকর্তার,",
                "তিনি যা সৃষ্টি করেছেন তার অনিষ্ট থেকে,",
                "এবং অন্ধকার রাতের অনিষ্ট থেকে যখন তা সমাগত হয়,",
                "এবং গ্রন্থিতে ফুৎকারকারিণীদের অনিষ্ট থেকে,",
                "এবং হিংসুকের অনিষ্ট থেকে যখন সে হিংসা করে।"
            )
            "ur" -> listOf(
                "کہہ دیجئے: میں صبح کے رب کی پناہ مانگتا ہوں،",
                "ہر اس چیز کی برائی سے جو اس نے پیدا کی،",
                "اور اندھیری رات کی برائی سے جب وہ چھا جائے،",
                "اور گرہوں میں پھونکنے والیوں کی برائی سے،",
                "اور حسد کرنے والے کی برائی سے جب وہ حسد کرے۔"
            )
            "zh" -> listOf(
                "你说：我求庇于曙光的主，",
                "免遭他所创造的万物的毒害；",
                "免遭黑夜笼罩时的毒害；",
                "免遭在结上吹气者（巫师）的毒害；",
                "免遭嫉妒者嫉妒时的毒害。"
            )
            "tr" -> listOf(
                "De ki: Yarattığı şeylerin şerrinden sabahın Rabbine sığınırım,",
                "Karanlığı çöktüğü zaman gecenin şerrinden,",
                "Düğümlere üfleyen büyücülerin şerrinden,",
                "Ve haset ettiği zaman hasetçinin şerrinden,",
                "O'na sığınırım."
            )
            "fr" -> listOf(
                "Dis: \"Je cherche protection auprès du Seigneur de l'aube naissante,",
                "contre le mal des êtres qu'Il a créés,",
                "contre le mal de l'obscurité quand elle s'approfondit,",
                "contre le mal de celles qui soufflent sur les nœuds,",
                "et contre le mal de l'envieux quand il envie.\""
            )
            "de" -> listOf(
                "Sprich: Ich nehme Zuflucht beim Herrn des Frühlichts",
                "vor dem Übel dessen, was Er erschaffen hat,",
                "vor dem Übel der Dunkelheit, wenn sie hereinbricht,",
                "vor dem Übel der Knotenanbläserinnen,",
                "und vor dem Übel eines Neiders, wenn er neidet."
            )
            else -> listOf(
                "Katakanlah, \"Aku berlindung kepada Tuhan yang menguasai subuh (fajar),",
                "dari kejahatan (makhluk yang) Dia ciptakan,",
                "dan dari kejahatan malam apabila telah gelap gulita,",
                "dan dari kejahatan (perempuan-perempuan) penyihir yang meniup pada buhul-buhul (talinya),",
                "dan dari kejahatan orang yang dengki apabila dia dengki.\""
            )
        }

        return listOf(
            AyahItem(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Qul a'udzu bi rabbil-falaq", translations[0], "https://everyayah.com/data/Alafasy_128kbps/113001.mp3"),
            AyahItem(113, 2, "مِن شَرِّ مَا خَلَقَ", "Min syarri ma khalaq", translations[1], "https://everyayah.com/data/Alafasy_128kbps/113002.mp3"),
            AyahItem(113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "Wa min syarri ghasiqin iza waqab", translations[2], "https://everyayah.com/data/Alafasy_128kbps/113003.mp3"),
            AyahItem(113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "Wa min syarrin-naffasati fil-'uqad", translations[3], "https://everyayah.com/data/Alafasy_128kbps/113004.mp3"),
            AyahItem(113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "Wa min syarri hasidin iza hasad", translations[4], "https://everyayah.com/data/Alafasy_128kbps/113005.mp3")
        )
    }

    private fun getAnNas(lang: String): List<AyahItem> {
        val translations = when (lang) {
            "en" -> listOf(
                "Say, \"I seek refuge in the Lord of mankind,",
                "The Sovereign of mankind,",
                "The God of mankind,",
                "From the evil of the retreating whisperer -",
                "Who whispers [evil] into the breasts of mankind -",
                "From among the jinn and mankind.\""
            )
            "ms" -> listOf(
                "Katakanlah (wahai Muhammad): \"Aku berlindung kepada Pemelihara sekalian manusia,",
                "\"Raja sekalian manusia,",
                "\"Tuhan sekalian manusia,",
                "\"Dari kejahatan pembisik penghasut yang timbul tenggelam,",
                "\"Yang melemparkan bisikan dan hasutannya ke dalam dada manusia,",
                "\"(Iaitu pembisik) dari kalangan jin dan manusia.\""
            )
            "ar" -> listOf(
                "قل أعوذ برب الناس",
                "ملك الناس",
                "إله الناس",
                "من شر الوسواس الخناس",
                "الذي يوسوس في صدور الناس",
                "من الجنة والناس"
            )
            "th" -> listOf(
                "จงกล่าวเถิด \"ข้าพระองค์ขอความคุ้มครองต่อพระผู้อภิบาลแห่งมนุษยชาติ",
                "พระราชาแห่งมนุษยชาติ",
                "พระผู้ได้รับการเคารพสักการะแห่งมนุษยชาติ",
                "ให้พ้นจากความชั่วร้ายของผู้กระซิบกระซาบที่หลบซ่อน",
                "ผู้ซึ่งกระซิบกระซาบในหัวอกของมนุษย์",
                "จากหมู่ญินและมนุษย์\""
            )
            "ja" -> listOf(
                "言ってやるがいい。「わたしは人間の主の加護を乞う。",
                "人間の統治者、",
                "人間の神の。",
                "隠れ忍ぶ囁き魔の悪より、",
                "人間の胸に囁きかけるものの悪より、",
                "ジンと人間の中から。」"
            )
            "hi" -> listOf(
                "कहो: \"मैं इंसानों के रब की शरण माँगता हूँ,",
                "इंसानों के बादशाह की,",
                "इंसानों के पूज्य की,",
                "पीछे हट जाने वाले वसवसा (कुविचार) डालने वाले की बुराई से,",
                "जो इंसानों के सीनों में वसवसा डालता है,",
                "जिन्नों में से और इंसानों में से।\""
            )
            "bn" -> listOf(
                "বলুন, আমি আশ্রয় চাই মানুষের পালনকর্তার কাছে,",
                "মানুষের অধিপতির কাছে,",
                "মানুষের মা'বুদের কাছে,",
                "আত্মগোপনকারী কুমন্ত্রণাদাতার অনিষ্ট থেকে,",
                "যে কুমন্ত্রণা দেয় মানুষের অন্তরে,",
                "জিনের মধ্য থেকে এবং মানুষের মধ্য থেকে।"
            )
            "ur" -> listOf(
                "کہہ دیجئے: میں انسانوں کے رب کی پناہ مانگتا ہوں،",
                "انسانوں کے بادشاہ کی،",
                "انسانوں کے معبود کی،",
                "وسوسہ ڈالنے والے پیچھے ہٹ جانے والے کی برائی سے،",
                "جو انسانوں کے سینوں میں وسوسے ڈالتا ہے،",
                "جنوں میں سے ہو یا انسانوں میں سے۔"
            )
            "zh" -> listOf(
                "你说：我求庇于人类的主宰，",
                "人类的君王，",
                "人类的神明，",
                "免遭潜伏的教唆者的毒害，",
                "他在人们的心中教唆，",
                "他属于幽灵或人类。"
            )
            "tr" -> listOf(
                "De ki: İnsanların Rabbine sığınırım,",
                "İnsanların hükümdarına,",
                "İnsanların İlahına,",
                "O sinsi vesvesecinin şerrinden,",
                "Ki o, insanların göğüslerine vesvese verir,",
                "Gerek cinlerden, gerek insanlardan!"
            )
            "fr" -> listOf(
                "Dis: \"Je cherche protection auprès du Seigneur des hommes,",
                "Le Souverain des hommes,",
                "Dieu des hommes,",
                "contre le mal du mauvais conseiller, furtif,",
                "qui souffle le mal dans les poitrines des hommes,",
                "qu'il soit dresseur de djinns ou homme.\""
            )
            "de" -> listOf(
                "Sprich: Ich nehme Zuflucht beim Herrn der Menschen,",
                "dem König der Menschen,",
                "dem Gott der Menschen,",
                "vor dem Übel des einflüsternden Verführers, der schleicht,",
                "der in die Brüste der Menschen einflüstert,",
                "unter den Dschinn und den Menschen."
            )
            else -> listOf(
                "Katakanlah, \"Aku berlindung kepada Tuhannya manusia,",
                "Raja manusia,",
                "sembahan manusia,",
                "dari kejahatan (bisikan) setan yang bersembunyi,",
                "yang membisikkan (kejahatan) ke dalam dada manusia,",
                "dari (golongan) jin dan manusia.\""
            )
        }

        return listOf(
            AyahItem(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Qul a'udzu birabbin-nas", translations[0], "https://everyayah.com/data/Alafasy_128kbps/114001.mp3"),
            AyahItem(114, 2, "مَلِكِ النَّاسِ", "Malikin-nas", translations[1], "https://everyayah.com/data/Alafasy_128kbps/114002.mp3"),
            AyahItem(114, 3, "إِلَٰهِ النَّاسِ", "Ilahin-nas", translations[2], "https://everyayah.com/data/Alafasy_128kbps/114003.mp3"),
            AyahItem(114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "Min syarril-waswasil-khannas", translations[3], "https://everyayah.com/data/Alafasy_128kbps/114004.mp3"),
            AyahItem(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Allazi yuwaswisu fi sudurin-nas", translations[4], "https://everyayah.com/data/Alafasy_128kbps/114005.mp3"),
            AyahItem(114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "Minal-jinnati wan-nas", translations[5], "https://everyayah.com/data/Alafasy_128kbps/114006.mp3")
        )
    }

    private fun getAlKautsar(lang: String): List<AyahItem> {
        val translations = when (lang) {
            "en" -> listOf(
                "Indeed, We have granted you, [O Muhammad], al-Kawthar.",
                "So pray to your Lord and sacrifice [to Him alone].",
                "Indeed, your enemy is the one cut off."
            )
            "ms" -> listOf(
                "Sesungguhnya Kami telah mengurniakan kepadamu (wahai Muhammad) kebaikan yang banyak.",
                "Oleh itu, kerjakanlah sembahyang kerana Tuhanmu semata-mata, dan sembelihlah korban.",
                "Sesungguhnya orang yang bencikan engkau, dialah yang terputus (dari rahmat Allah)."
            )
            "ar" -> listOf(
                "إنا أعطيناك الكوثر",
                "فصل لربك وانحر",
                "إن شانئك هو الأبتر"
            )
            "th" -> listOf(
                "แท้จริงเราได้ประทานความดีงามอันมากมายแก่เจ้า (มุฮัมมัด)",
                "ดังนั้นจงละหมาดเพื่อพระผู้อภิบาลของเจ้าและจงเชือดสัตว์พลี",
                "แท้จริงผู้ที่เกลียดชังเจ้านั้น เขาคือผู้ถูกตัดขาด"
            )
            "ja" -> listOf(
                "本当にわれは、無数の恵みをあなたに授けた。",
                "だからあなたの主のために礼拝し、犠牲を捧げなさい。",
                "本当にあなたを憎む者こそ、子孫の絶える者である。"
            )
            "hi" -> listOf(
                "निश्चय ही हमने तुम्हें अल-कौसर (असीम भलाई) प्रदान की।",
                "अतः अपने रब के लिए नमाज़ पढ़ो और क़ुरबानी करो।",
                "निश्चय ही तुम्हारा शत्रु ही बेनाम-ओ-निशान रहेगा।"
            )
            "bn" -> listOf(
                "নিশ্চয়ই আমরা আপনাকে কাওসার (প্রচুর কল্যাণ) দান করেছি।",
                "অতএব আপনার পালনকর্তার উদ্দেশ্যে নামাজ পড়ুন এবং কোরবানি করুন।",
                "নিশ্চয়ই আপনার শত্রু নির্বংশ হবে।"
            )
            "ur" -> listOf(
                "بے شک ہم نے آپ کو کوثر (بہت زیادہ بھلائی) عطا فرمائی۔",
                "پس اپنے رب کے لیے نماز پڑھیں اور قربانی کریں۔",
                "بے شک آپ کا دشمن ہی بے نام و نشان رہے گا۔"
            )
            "zh" -> listOf(
                "我确已赐你多福，",
                "故你应当为主而礼拜，并宰牲。",
                "怨恨你者，确是绝后的。"
            )
            "tr" -> listOf(
                "Şüphesiz biz sana Kevser'i verdik.",
                "Şimdi sen Rabbine kulluk et ve kurban kes.",
                "Asıl sonu kesik olan, sana buğzedendir."
            )
            "fr" -> listOf(
                "Nous t'avons certes accordé l'Abondance.",
                "Accomplis la Salât pour ton Seigneur et sacrifie.",
                "Celui qui te hait sera certes sans postérité."
            )
            "de" -> listOf(
                "Wahrlich, Wir haben dir die Fülle des Guten gegeben.",
                "So bete zu deinem Herrn und schlachte.",
                "Gewiss, der dich hasst, ist der Abgeschnittene."
            )
            else -> listOf(
                "Sungguh, Kami telah memberimu (Muhammad) nikmat yang banyak.",
                "Maka laksanakanlah sholat karena Tuhanmu, dan berkurbanlah.",
                "Sungguh, orang-orang yang membencimu dialah yang terputus (dari rahmat Allah)."
            )
        }

        return listOf(
            AyahItem(108, 1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "Inna a'tainakal-kausar", translations[0], "https://everyayah.com/data/Alafasy_128kbps/108001.mp3"),
            AyahItem(108, 2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "Fa salli li rabbika wan-har", translations[1], "https://everyayah.com/data/Alafasy_128kbps/108002.mp3"),
            AyahItem(108, 3, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", "Inna syani'aka huwal-abtar", translations[2], "https://everyayah.com/data/Alafasy_128kbps/108003.mp3")
        )
    }

    private fun getAlAsr(lang: String): List<AyahItem> {
        val translations = when (lang) {
            "en" -> listOf(
                "By time,",
                "Indeed, mankind is in loss,",
                "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience."
            )
            "ms" -> listOf(
                "Demi Masa!",
                "Sesungguhnya manusia itu dalam kerugian;",
                "Kecuali orang-orang yang beriman dan beramal soleh, serta berpesan-pesan dengan kebenaran dan berpesan-pesan dengan sabar."
            )
            "ar" -> listOf(
                "والعصر",
                "إن الإنسان لفي خسر",
                "إلا الذين آمنوا وعملوا الصالحات وتواصوا بالحق وتواصوا بالصبر"
            )
            "th" -> listOf(
                "ขอสาบานด้วยกาลเวลา",
                "แท้จริงมนุษย์นั้นอยู่ในการขาดทุน",
                "เว้นแต่บรรดาผู้ศรัทธาและกระทำความดีทั้งหลาย และตักเตือนกันในสัจธรรม และตักเตือนกันให้อดทน"
            )
            "ja" -> listOf(
                "時代（の推移）にかけて（誓う）。",
                "本当に人間は、損失の中にいる。",
                "信じて善行に勤しみ、互いに真理を勧め合い、また忍耐を勧め合う者たちの外は。"
            )
            "hi" -> listOf(
                "ज़माने की क़सम!",
                "बेशक इंसान घाटे में है,",
                "सिवाय उन लोगों के जो ईमान लाए और उन्होंने अच्छे कर्म किए और एक-दूसरे को हक़ की वसीयत की और सब्र की ताकीद की।"
            )
            "bn" -> listOf(
                "মহাকালের শপথ!",
                "নিশ্চয়ই মানুষ ক্ষতির মধ্যে নিমজ্জিত,",
                "তারা ছাড়া যারা ঈমান এনেছে, সৎকর্ম করেছে এবং পরস্পরকে সত্যের উপদেশ দিয়েছে ও ধৈর্যের উপদেশ দিয়েছে।"
            )
            "ur" -> listOf(
                "زمانے کی قسم!",
                "بے شک انسان خسارے میں ہے،",
                "سوائے ان لوگوں کے جو ایمان لائے اور نیک اعمال کیے اور ایک دوسرے کو حق کی وصیت کی اور صبر کی تاکید کی۔"
            )
            "zh" -> listOf(
                "誓以时光，",
                "一切人确是在亏折之中，",
                "惟信道而且行善，并以真理相劝，以坚忍相勉的人则不然。"
            )
            "tr" -> listOf(
                "Asra yemin olsun ki,",
                "İnsan gerçekten ziyan içindedir.",
                "Ancak iman edip salih ameller işleyenler, birbirlerine hakkı tavsiye edenler ve sabrı tavsiye edenler müstesnadır."
            )
            "fr" -> listOf(
                "Par le Temps !",
                "L'homme est certes, en perdition,",
                "sauf ceux qui croient et accomplissent les bonnes œuvres, s'enjoignent mutuellement la vérité et s'enjoignent mutuellement l'endurance."
            )
            "de" -> listOf(
                "Beim Nachmittag (oder bei der Zeit)!",
                "Wahrlich, der Mensch ist in Verlust,",
                "außer denjenigen, die glauben und rechtschaffene Werke tun und einander zur Wahrheit und zur Geduld ermahnen."
            )
            else -> listOf(
                "Demi masa.",
                "Sungguh, manusia berada dalam kerugian,",
                "kecuali orang-orang yang beriman dan mengerjakan kebajikan serta saling menasihati untuk kebenaran dan saling menasihati untuk kesabaran."
            )
        }

        return listOf(
            AyahItem(103, 1, "وَالْعَصْرِ", "Wal-'asr", translations[0], "https://everyayah.com/data/Alafasy_128kbps/103001.mp3"),
            AyahItem(103, 2, "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ", "Innal-insana lafi khusr", translations[1], "https://everyayah.com/data/Alafasy_128kbps/103002.mp3"),
            AyahItem(103, 3, "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ", "Illallazina amanu wa 'amilus-salihati wa tawasaubil-haqqi wa tawasaubis-sabr", translations[2], "https://everyayah.com/data/Alafasy_128kbps/103003.mp3")
        )
    }

    private fun getAlMulk(lang: String): List<AyahItem> {
        val translations = when (lang) {
            "en" -> listOf(
                "Blessed is He in whose hand is dominion, and He is over all things competent -",
                "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -",
                "[And] who created seven heavens in layers. You do not see in the creation of the Most Merciful any inconsistency. So return [your] vision [to the sky]; do you see any breaks?"
            )
            "ms" -> listOf(
                "Maha Berkat (serta Maha Tinggilah kelebihan) Tuhan yang menguasai kerajaan (pemerintahan); dan Dia Maha Kuasa atas tiap-tiap sesuatu.",
                "Dia lah yang telah mentakdirkan adanya mati dan hidup untuk menguji kamu: siapakah di antara kamu yang lebih baik amalnya; dan Dia Maha Kuasa, lagi Maha Pengampun.",
                "Dia lah yang telah menciptakan tujuh petala langit berlapis-lapis; kamu tidak melihat sebarang cacat cela pada ciptaan Yang Maha Pemurah itu; maka ulangilah pandangan: Adakah kamu melihat sebarang rekahan?"
            )
            "ar" -> listOf(
                "تبارك الذي بيده الملك وهو على كل شيء قدير",
                "الذي خلق الموت والحياة ليبلوكم أيكم أحسن عملا وهو العزيز الغفور",
                "الذي خلق سبع سماوات طباقا ما ترى في خلق الرحمن من تفاوت فارجع البصر هل ترى من فطور"
            )
            "th" -> listOf(
                "มหาบริสุทธิ์ยิ่งแด่พระองค์ผู้ซึ่งอำนาจแห่งการปกครองอยู่ในพระหัตถ์ของพระองค์ และพระองค์ทรงอานุภาพเหนือทุกสิ่งทุกอย่าง",
                "พระผู้ทรงสร้างความตายและชีวิตเพื่อทรงทดสอบพวกเจ้าว่าผู้ใดในหมู่พวกเจ้ามีผลงานดียิ่ง และพระองค์ทรงเดชานุภาพ ผู้ทรงให้อภัยเสมอ",
                "พระผู้ทรงสร้างชั้นฟ้าทั้งเจ็ดซ้อนกันเป็นชั้นๆ เจ้าจะไม่เห็นสิ่งผิดส่วนใดๆ ในการสร้างของพระผู้ทรงกรุณาปรานี จงเหลียวมองอีกครั้งเถิด เจ้าเห็นรอยร้าวใดๆ หรือไม่"
            )
            "ja" -> listOf(
                "主権を掌握される御方に祝福あれ。本当にかれは凡てのことに全能であられる。",
                "かれは死と生を創造された。あなたがたの中誰の行いが最も優れているかを試みるために。かれは偉力ならびなき御方、寛容にして赦される御方である。",
                "かれは七つの天を層にして創造された。あなたは慈悲あまねき御方の創造に何の不調和も見出すことはできない。もう一度目を向けなさい。何かの欠陥が見出せるか。"
            )
            "hi" -> listOf(
                "बरकत वाला है वह जिसके हाथ में बादशाही है और वह हर चीज़ पर पूरी ताक़त रखता है।",
                "जिसने मौत और ज़िन्दगी को पैदा किया ताकि तुम्हें आज़माए कि तुममें से किसका अमल सबसे अच्छा है। और वह ज़बरदस्त और बड़ा बख़्शने वाला है।",
                "जिसने सात आसमान तह-बर-तह बनाए। तू दयालु की रचना में कोई कमी न देखेगा। फिर नज़र दौड़ाकर देख, क्या तुझे कोई शिगाफ़ दिखाई देता है?"
            )
            "bn" -> listOf(
                "বরকতময় তিনি যাঁর হাতে সর্বময় কর্তৃত্ব, এবং তিনি সর্ববিষয়ে সর্বশক্তিমান।",
                "যিনি সৃষ্টি করেছেন মরণ ও জীবন, যাতে তোমাদের পরীক্ষা করেন কে তোমাদের মধ্যে কর্মে শ্রেষ্ঠ? তিনি পরাক্রমশালী, ক্ষমাশীল।",
                "যিনি সৃষ্টি করেছেন স্তরে স্তরে সাত আসমান। পরম করুণাময়ের সৃষ্টিতে তুমি কোন অসঙ্গতি দেখতে পাবে না। আবার তাকিয়ে দেখ, কোন ত্রুটি দেখতে পাও কি?"
            )
            "ur" -> listOf(
                "بابرکت ہے وہ ذات جس کے ہاتھ میں بادشاہی ہے اور وہ ہر چیز پر قادر ہے۔",
                "جس نے موت اور زندگی کو پیدا کیا تاکہ تمہیں آزمائے کہ تم میں سے اچھے عمل والا کون ہے۔ اور وہ زبردست، بخشنے والا ہے۔",
                "جس نے سات آسمان اوپر نیچے بنائے۔ تو رحمن کی تخلیق میں کوئی خلل نہیں دیکھے گا۔ پھر نظر دہرا کر دیکھ، کیا تجھے کوئی شگاف نظر آتا ہے؟"
            )
            "zh" -> listOf(
                "多福哉拥有主权的主！他对万事是全能的。",
                "他曾创造了死与生，以便考验你们中谁的品行是最好的。他是万能的，是至赦的。",
                "他创造了七层天，你看不到至仁主的造化有任何不协调。你再看一看，你究竟能看到任何裂痕吗？"
            )
            "tr" -> listOf(
                "Hükümranlık elinde olan Allah yüceler yücesidir ve O her şeye kadirdir.",
                "O, hanginizin daha güzel amel yapacağını sınamak için ölümü ve hayatı yaratandır. O, mutlak güç sahibidir, çok bağışlayandır.",
                "O, yedi göğü tabaka tabaka yaratandır. Rahmân'ın yaratışında hiçbir uyumsuzluk göremezsin. Bir kere daha bak, hiçbir çatlak görebiliyor musun?"
            )
            "fr" -> listOf(
                "Béni soit celui dans la main de qui est la royauté, et Il est Omnipotent.",
                "Celui qui a créé la mort et la vie afin de vous éprouver [et de savoir] qui de vous est le meilleur en œuvre, et c'est Lui le Puissant, le Pardonneur.",
                "Celui qui a créé sept cieux superposés sans que tu voies de disproportion en la création du Tout Miséricordieux. Ramène [sur elle] le regard. Y vois-tu une brèche quelconque?"
            )
            "de" -> listOf(
                "Segensreich ist Derjenige, in Dessen Hand die Herrschaft ist; und Er hat zu allem die Macht.",
                "(Er,) Der den Tod und das Leben erschaffen hat, damit Er euch prüfe, wer von euch die besten Taten verrichtet; und Er ist der Allmächtige, der Allvergebende.",
                "Der sieben Himmel in Schichten erschaffen hat. Du siehst in der Schöpfung des Allerbarmers kein Missverhältnis. Wende deinen Blick nochmals: Siehst du irgendeinen Riss?"
            )
            else -> listOf(
                "Mahasuci Allah yang menguasai (segala) kerajaan, dan Dia Mahakuasa atas segala sesuatu.",
                "Yang menciptakan mati dan hidup, untuk menguji kamu, siapa di antara kamu yang lebih baik amalnya. Dan Dia Mahaperkasa, Maha Pengampun.",
                "Yang menciptakan tujuh langit berlapis-lapis. Tidak akan kamu lihat sesuatu yang tidak seimbang pada ciptaan Tuhan Yang Maha Pengasih. Maka lihatlah sekali lagi, adakah kamu lihat sesuatu yang cacat?"
            )
        }

        return listOf(
            AyahItem(67, 1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Tabarakallazi biyadihil-mulku wa huwa 'ala kulli syai'in qadir", translations[0], "https://everyayah.com/data/Alafasy_128kbps/067001.mp3"),
            AyahItem(67, 2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "Allazi khalaqal-mauta wal-hayata liyabluwakum ayyukum ahsanu 'amala, wa huwal-'azizul-gafur", translations[1], "https://everyayah.com/data/Alafasy_128kbps/067002.mp3"),
            AyahItem(67, 3, "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَّا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِن تَفَاوُتٍ ۖ فَارْجِعِ الْبَصَرَ هَلْ تَرَىٰ مِن فُطُورٍ", "Allazi khalaqa sab'a samawatin tibaqa, ma tara fi khalqir-rahmani min tafawut, farji'il-basara hal tara min futur", translations[2], "https://everyayah.com/data/Alafasy_128kbps/067003.mp3")
        )
    }
}
