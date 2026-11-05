package com.jalanislam.app.data.quran

object CuratedQuranVerses {

    fun getVerses(surahNumber: Int): List<AyahItem> {
        return when (surahNumber) {
            1 -> listOf(
                AyahItem(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "Bismillahir-rahmanir-rahim", "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.", "https://everyayah.com/data/Alafasy_128kbps/001001.mp3"),
                AyahItem(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "Al-hamdu lillahi rabbil-'alamin", "Segala puji bagi Allah, Tuhan semesta alam.", "https://everyayah.com/data/Alafasy_128kbps/001002.mp3"),
                AyahItem(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "Ar-rahmanir-rahim", "Maha Pengasih, Maha Penyayang.", "https://everyayah.com/data/Alafasy_128kbps/001003.mp3"),
                AyahItem(1, 4, "مَالِكِ يَوْمِ الدِّينِ", "Maliki yaumid-din", "Pemilik hari pembalasan.", "https://everyayah.com/data/Alafasy_128kbps/001004.mp3"),
                AyahItem(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "Iyyaka na'budu wa iyyaka nasta'in", "Hanya kepada Engkaulah kami menyembah dan hanya kepada Engkaulah kami mohon pertolongan.", "https://everyayah.com/data/Alafasy_128kbps/001005.mp3"),
                AyahItem(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Ihdinas-siratal-mustaqim", "Tunjukilah kami jalan yang lurus,", "https://everyayah.com/data/Alafasy_128kbps/001006.mp3"),
                AyahItem(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "Siratalladhina an'amta 'alaihim gairil-magdubi 'alaihim wa lad-dallin", "(yaitu) jalan orang-orang yang telah Engkau beri nikmat kepadanya; bukan (jalan) mereka yang dimurkai, dan bukan (pula jalan) mereka yang sesat.", "https://everyayah.com/data/Alafasy_128kbps/001007.mp3")
            )
            112 -> listOf(
                AyahItem(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Qul huwallahu ahad", "Katakanlah (Muhammad): \"Dialah Allah, Yang Maha Esa.\"", "https://everyayah.com/data/Alafasy_128kbps/112001.mp3"),
                AyahItem(112, 2, "اللَّهُ الصَّمَدُ", "Allahus-samad", "Allah tempat meminta segala sesuatu.", "https://everyayah.com/data/Alafasy_128kbps/112002.mp3"),
                AyahItem(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "Lam yalid wa lam yulad", "(Allah) tidak beranak dan tidak pula diperanakkan,", "https://everyayah.com/data/Alafasy_128kbps/112003.mp3"),
                AyahItem(112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Wa lam yakun lahu kufuwan ahad", "dan tidak ada sesuatu yang setara dengan Dia.", "https://everyayah.com/data/Alafasy_128kbps/112004.mp3")
            )
            113 -> listOf(
                AyahItem(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Qul a'udzu bi rabbil-falaq", "Katakanlah, \"Aku berlindung kepada Tuhan yang menguasai subuh (fajar),", "https://everyayah.com/data/Alafasy_128kbps/113001.mp3"),
                AyahItem(113, 2, "مِن شَرِّ مَا خَلَقَ", "Min syarri ma khalaq", "dari kejahatan (makhluk yang) Dia ciptakan,", "https://everyayah.com/data/Alafasy_128kbps/113002.mp3"),
                AyahItem(113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "Wa min syarri ghasiqin iza waqab", "dan dari kejahatan malam apabila telah gelap gulita,", "https://everyayah.com/data/Alafasy_128kbps/113003.mp3"),
                AyahItem(113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "Wa min syarrin-naffasati fil-'uqad", "dan dari kejahatan (perempuan-perempuan) penyihir yang meniup pada buhul-buhul (talinya),", "https://everyayah.com/data/Alafasy_128kbps/113004.mp3"),
                AyahItem(113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "Wa min syarri hasidin iza hasad", "dan dari kejahatan orang yang dengki apabila dia dengki.\"", "https://everyayah.com/data/Alafasy_128kbps/113005.mp3")
            )
            114 -> listOf(
                AyahItem(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Qul a'udzu birabbin-nas", "Katakanlah, \"Aku berlindung kepada Tuhannya manusia,", "https://everyayah.com/data/Alafasy_128kbps/114001.mp3"),
                AyahItem(114, 2, "مَلِكِ النَّاسِ", "Malikin-nas", "Raja manusia,", "https://everyayah.com/data/Alafasy_128kbps/114002.mp3"),
                AyahItem(114, 3, "إِلَٰهِ النَّاسِ", "Ilahin-nas", "sembahan manusia,", "https://everyayah.com/data/Alafasy_128kbps/114003.mp3"),
                AyahItem(114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "Min syarril-waswasil-khannas", "dari kejahatan (bisikan) setan yang bersembunyi,", "https://everyayah.com/data/Alafasy_128kbps/114004.mp3"),
                AyahItem(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Allazi yuwaswisu fi sudurin-nas", "yang membisikkan (kejahatan) ke dalam dada manusia,", "https://everyayah.com/data/Alafasy_128kbps/114005.mp3"),
                AyahItem(114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "Minal-jinnati wan-nas", "dari (golongan) jin dan manusia.\"", "https://everyayah.com/data/Alafasy_128kbps/114006.mp3")
            )
            108 -> listOf(
                AyahItem(108, 1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "Inna a'tainakal-kausar", "Sungguh, Kami telah memberimu (Muhammad) nikmat yang banyak.", "https://everyayah.com/data/Alafasy_128kbps/108001.mp3"),
                AyahItem(108, 2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "Fa salli li rabbika wan-har", "Maka laksanakanlah sholat karena Tuhanmu, dan berkurbanlah.", "https://everyayah.com/data/Alafasy_128kbps/108002.mp3"),
                AyahItem(108, 3, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", "Inna syani'aka huwal-abtar", "Sungguh, orang-orang yang membencimu dialah yang terputus (dari rahmat Allah).", "https://everyayah.com/data/Alafasy_128kbps/108003.mp3")
            )
            103 -> listOf(
                AyahItem(103, 1, "وَالْعَصْرِ", "Wal-'asr", "Demi masa.", "https://everyayah.com/data/Alafasy_128kbps/103001.mp3"),
                AyahItem(103, 2, "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ", "Innal-insana lafi khusr", "Sungguh, manusia berada dalam kerugian,", "https://everyayah.com/data/Alafasy_128kbps/103002.mp3"),
                AyahItem(103, 3, "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ", "Illallazina amanu wa 'amilus-salihati wa tawasaubil-haqqi wa tawasaubis-sabr", "kecuali orang-orang yang beriman dan mengerjakan kebajikan serta saling menasihati untuk kebenaran dan saling menasihati untuk kesabaran.", "https://everyayah.com/data/Alafasy_128kbps/103003.mp3")
            )
            67 -> listOf(
                AyahItem(67, 1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Tabarakallazi biyadihil-mulku wa huwa 'ala kulli syai'in qadir", "Mahasuci Allah yang menguasai (segala) kerajaan, dan Dia Mahakuasa atas segala sesuatu.", "https://everyayah.com/data/Alafasy_128kbps/067001.mp3"),
                AyahItem(67, 2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "Allazi khalaqal-mauta wal-hayata liyabluwakum ayyukum ahsanu 'amala, wa huwal-'azizul-gafur", "Yang menciptakan mati dan hidup, untuk menguji kamu, siapa di antara kamu yang lebih baik amalnya. Dan Dia Mahaperkasa, Maha Pengampun.", "https://everyayah.com/data/Alafasy_128kbps/067002.mp3"),
                AyahItem(67, 3, "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَّا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِن تَفَاوُتٍ ۖ فَارْجِعِ الْبَصَرَ هَلْ تَرَىٰ مِن فُطُورٍ", "Allazi khalaqa sab'a samawatin tibaqa, ma tara fi khalqir-rahmani min tafawut, farji'il-basara hal tara min futur", "Yang menciptakan tujuh langit berlapis-lapis. Tidak akan kamu lihat sesuatu yang tidak seimbang pada ciptaan Tuhan Yang Maha Pengasih. Maka lihatlah sekali lagi, adakah kamu lihat sesuatu yang cacat?", "https://everyayah.com/data/Alafasy_128kbps/067003.mp3")
            )
            else -> emptyList()
        }
    }
}

