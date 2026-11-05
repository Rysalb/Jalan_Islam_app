package com.jalanislam.app.data.guide

object PrayerGuideRepository {

    val items: List<PrayerGuideItem> = listOf(
        // === 1. PANDUAN WUDHU ===
        PrayerGuideItem(
            id = "wudhu_niat",
            category = GuideCategory.WUDHU,
            title = "Niat Berwudhu",
            arabic = "نَوَيْتُ الْوُضُوْءَ لِرَفْعِ الْحَدَثِ الْأَصْغَرِ فَرْضًا لِلّٰهِ تَعَالَى",
            latin = "Nawaitul wudhuu-a lirof'il hadatsil ashghari fardhon lillaahi ta'aala.",
            translation = "Saya berniat berwudhu untuk menghilangkan hadats kecil, fardhu karena Allah Ta'ala.",
            note = "Dibaca dalam hati bersamaan saat pertama kali membasuh air ke wajah.",
            stepNumber = 1
        ),
        PrayerGuideItem(
            id = "wudhu_step_1",
            category = GuideCategory.WUDHU,
            title = "Mencuci Telapak Tangan & Sela Jari",
            arabic = "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ",
            latin = "Bismillaahir-rahmaanir-rahiim.",
            translation = "Dengan menyebut nama Allah Yang Maha Pengasih lagi Maha Penyayang.",
            note = "Sunnah membaca basmalah dan mencuci kedua telapak tangan hingga pergelangan 3 kali serta menyela-nyela jari jemari.",
            stepNumber = 2
        ),
        PrayerGuideItem(
            id = "wudhu_step_2",
            category = GuideCategory.WUDHU,
            title = "Berkumur-kumur (Madhmadhoh)",
            arabic = "اَللّٰهُمَّ أَعِنِّيْ عَلَى ذِكْرِكَ وَشُكْرِكَ وَحُسْنِ عِبَادَتِكَ",
            latin = "Allaahumma a'innii 'alaa dzikrika wa syukrika wa husni 'ibaadatik.",
            translation = "Ya Allah, tolonglah aku untuk senantiasa mengingat-Mu, bersyukur kepada-Mu, dan beribadah dengan baik kepada-Mu.",
            note = "Sunnah berkumur-kumur sebanyak 3 kali untuk membersihkan sisa makanan di rongga mulut.",
            stepNumber = 3
        ),
        PrayerGuideItem(
            id = "wudhu_step_3",
            category = GuideCategory.WUDHU,
            title = "Menghirup Air ke Hidung (Istinsyaq)",
            arabic = "اَللّٰهُمَّ أَرِحْنِيْ رَائِحَةَ الْجَنَّةِ",
            latin = "Allaahumma arihnii raa-ihatal jannah.",
            translation = "Ya Allah, ciumkanlah kepadaku wewangian surga.",
            note = "Menghirup air ke dalam lubang hidung kemudian mengeluarkannya (istintsar) sebanyak 3 kali.",
            stepNumber = 4
        ),
        PrayerGuideItem(
            id = "wudhu_step_4",
            category = GuideCategory.WUDHU,
            title = "Membasuh Wajah (Rukun Wudhu)",
            arabic = "اَللّٰهُمَّ بَيِّضْ وَجْهِيْ يَوْمَ تَبْيَضُّ وُجُوْهٌ وَتَسْوَدُّ وُجُوْهٌ",
            latin = "Allaahumma bayyidh wajhii yawma tabyadh-dhu wujuuhun wa taswaddu wujuuh.",
            translation = "Ya Allah, putihkanlah wajahku pada hari ketika wajah-wajah menjadi putih berseri dan wajah-wajah menjadi hitam legam.",
            note = "Rukun wajib wudhu: Membasuh seluruh wajah dari batas tumbuhnya rambut kepala hingga bawah dagu, dan dari telinga kanan ke telinga kiri sebanyak 3 kali.",
            stepNumber = 5
        ),
        PrayerGuideItem(
            id = "wudhu_step_5",
            category = GuideCategory.WUDHU,
            title = "Membasuh Kedua Tangan Sampai Siku",
            arabic = "اَللّٰهُمَّ أَعْطِنِيْ كِتَابِيْ بِيَمِيْنِيْ وَحَاسِبْنِيْ حِسَابًا يَسِيْرًا",
            latin = "Allaahumma a'thinii kitaabii bi yamiinii wa haasibnii hisaaban yasiiraa.",
            translation = "Ya Allah, berikanlah kitab amalku dari sebelah kananku dan hisablah aku dengan hisab yang mudah.",
            note = "Rukun wajib wudhu: Membasuh tangan kanan lalu tangan kiri mulai dari ujung jari hingga melewati kedua siku sebanyak 3 kali.",
            stepNumber = 6
        ),
        PrayerGuideItem(
            id = "wudhu_step_6",
            category = GuideCategory.WUDHU,
            title = "Mengusap Sebagian Rambut Kepala",
            arabic = "اَللّٰهُمَّ حَرِّمْ شَعْرِيْ وَبَشَرِيْ عَلَى النَّارِ",
            latin = "Allaahumma harrim sya'rii wa basyarii 'alan-naar.",
            translation = "Ya Allah, haramkanlah rambut dan kulitku dari jilatan api neraka.",
            note = "Rukun wajib wudhu: Mengusap sebagian rambut atau kepala dengan kedua telapak tangan yang basah sebanyak 3 kali.",
            stepNumber = 7
        ),
        PrayerGuideItem(
            id = "wudhu_step_7",
            category = GuideCategory.WUDHU,
            title = "Mengusap Kedua Daun Telinga",
            arabic = "اَللّٰهُمَّ اجْعَلْنِيْ مِنَ الَّذِيْنَ يَسْتَمِعُوْنَ الْقَوْلَ فَيَتَّبِعُوْنَ أَحْسَنَهُ",
            latin = "Allaahummaj'alnii minal ladziina yastami'uunal qawla fayattabi'uuna ahsanah.",
            translation = "Ya Allah, jadikanlah aku termasuk orang-orang yang mendengarkan perkataan lalu mengikuti yang terbaik di antaranya.",
            note = "Sunnah memasukkan jari telunjuk ke liang telinga dan ibu jari mengusap bagian luar daun telinga sebanyak 3 kali.",
            stepNumber = 8
        ),
        PrayerGuideItem(
            id = "wudhu_step_8",
            category = GuideCategory.WUDHU,
            title = "Membasuh Kedua Kaki Sampai Mata Kaki",
            arabic = "اَللّٰهُمَّ ثَبِّتْ قَدَمَيَّ عَلَى الصِّرَاطِ يَوْمَ تَزِلُّ فِيْهِ الْأَقْدَامُ",
            latin = "Allaahumma tsabbit qadamayya 'alash-shiraathi yawma tazillu fiihil aqdaam.",
            translation = "Ya Allah, kokohkanlah kedua telapak kakiku di atas jembatan shirath pada hari tergelincirnya kaki-kaki manusia.",
            note = "Rukun wajib wudhu: Membasuh kaki kanan lalu kiri mulai dari ujung jari jemari hingga merata di atas mata kaki sebanyak 3 kali.",
            stepNumber = 9
        ),
        PrayerGuideItem(
            id = "wudhu_doa_setelah",
            category = GuideCategory.WUDHU,
            title = "Doa Sesudah Berwudhu",
            arabic = "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللّٰهُ وَحْدَهُ لَا شَرِيْكَ لَهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُوْلُهُ، اَللّٰهُمَّ اجْعَلْنِيْ مِنَ التَّوَّابِيْنَ وَاجْعَلْنِيْ مِنَ الْمُتَطَهِّرِيْنَ وَاجْعَلْنِيْ مِنْ عِبَادِكَ الصَّالِحِيْنَ",
            latin = "Asyhadu an laa ilaaha illallaahu wahdahu laa syariika lah, wa asyhadu anna Muhammadan 'abduhu wa rasuuluh. Allaahummaj'alnii minat-tawwaabiina waj'alnii minal mutathahhiriina waj'alnii min 'ibaadikash-shaalihiin.",
            translation = "Aku bersaksi tiada Tuhan selain Allah Yang Maha Esa tiada sekutu bagi-Nya, dan aku bersaksi Nabi Muhammad adalah hamba dan utusan-Nya. Ya Allah jadikanlah aku termasuk golongan orang yang bertaubat, bersuci, dan hamba-hamba-Mu yang shalih.",
            note = "Menghadap kiblat seraya mengangkat kedua tangan. Keutamaannya dibukakan 8 pintu surga dan boleh masuk dari pintu mana saja yang dikehendaki (HR. Muslim).",
            stepNumber = 10
        ),

        // === 2. NIAT SHOLAT 5 WAKTU ===
        PrayerGuideItem(
            id = "niat_subuh",
            category = GuideCategory.NIAT,
            title = "Niat Sholat Subuh (2 Rakaat)",
            arabic = "أُصَلِّيْ فَرْضَ الصُّبْحِ رَكْعَتَيْنِ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً لِلّٰهِ تَعَالَى\n(مَأْمُوْمًا / إِمَامًا لِلّٰهِ تَعَالَى)",
            latin = "Ushollii fardhash-shubhi rok'ataini mustaqbilal qiblati adaa-an lillaahi ta'aala. (Makmuuman / Imaaman lillaahi ta'aala).",
            translation = "Saya berniat sholat fardhu Subuh dua rakaat menghadap kiblat karena Allah Ta'ala. (Sebagai makmum / Sebagai imam).",
            note = "Waktu Subuh dimulai sejak fajar shadiq hingga terbit matahari. Disunnahkan membaca doa Qunut pada rakaat kedua setelah ruku'."
        ),
        PrayerGuideItem(
            id = "niat_dzuhur",
            category = GuideCategory.NIAT,
            title = "Niat Sholat Dzuhur (4 Rakaat)",
            arabic = "أُصَلِّيْ فَرْضَ الظُّهْرِ أَرْبَعَ رَكَعَاتٍ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً لِلّٰهِ تَعَالَى\n(مَأْمُوْمًا / إِمَامًا لِلّٰهِ تَعَالَى)",
            latin = "Ushollii fardhadh-dhuhri arba'a raka'aatin mustaqbilal qiblati adaa-an lillaahi ta'aala. (Makmuuman / Imaaman lillaahi ta'aala).",
            translation = "Saya berniat sholat fardhu Dzuhur empat rakaat menghadap kiblat karena Allah Ta'ala. (Sebagai makmum / Sebagai imam).",
            note = "Dimulai sejak matahari tergelincir dari puncaknya (zawal) ke arah barat hingga bayangan sama panjang dengan bendanya."
        ),
        PrayerGuideItem(
            id = "niat_ashar",
            category = GuideCategory.NIAT,
            title = "Niat Sholat Ashar (4 Rakaat)",
            arabic = "أُصَلِّيْ فَرْضَ الْعَصْرِ أَرْبَعَ رَكَعَاتٍ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً لِلّٰهِ تَعَالَى\n(مَأْمُوْمًا / إِمَامًا لِلّٰهِ تَعَالَى)",
            latin = "Ushollii fardhal 'ashri arba'a raka'aatin mustaqbilal qiblati adaa-an lillaahi ta'aala. (Makmuuman / Imaaman lillaahi ta'aala).",
            translation = "Saya berniat sholat fardhu Ashar empat rakaat menghadap kiblat karena Allah Ta'ala. (Sebagai makmum / Sebagai imam).",
            note = "Dimulai sejak bayangan benda melebihi panjang bendanya hingga matahari terbenam (sore hari)."
        ),
        PrayerGuideItem(
            id = "niat_maghrib",
            category = GuideCategory.NIAT,
            title = "Niat Sholat Maghrib (3 Rakaat)",
            arabic = "أُصَلِّيْ فَرْضَ الْمَغْرِبِ ثَلَاثَ رَكَعَاتٍ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً لِلّٰهِ تَعَالَى\n(مَأْمُوْمًا / إِمَامًا لِلّٰهِ تَعَالَى)",
            latin = "Ushollii fardhal maghribi tsalaatsa raka'aatin mustaqbilal qiblati adaa-an lillaahi ta'aala. (Makmuuman / Imaaman lillaahi ta'aala).",
            translation = "Saya berniat sholat fardhu Maghrib tiga rakaat menghadap kiblat karena Allah Ta'ala. (Sebagai makmum / Sebagai imam).",
            note = "Dimulai tepat saat piringan matahari terbenam sepenuhnya di cakrawala barat hingga mega merah (syafaq) menghilang."
        ),
        PrayerGuideItem(
            id = "niat_isya",
            category = GuideCategory.NIAT,
            title = "Niat Sholat Isya (4 Rakaat)",
            arabic = "أُصَلِّيْ فَرْضَ الْعِشَاءِ أَرْبَعَ رَكَعَاتٍ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً لِلّٰهِ تَعَالَى\n(مَأْمُوْمًا / إِمَامًا لِلّٰهِ تَعَالَى)",
            latin = "Ushollii fardhal 'isyaa-i arba'a raka'aatin mustaqbilal qiblati adaa-an lillaahi ta'aala. (Makmuuman / Imaaman lillaahi ta'aala).",
            translation = "Saya berniat sholat fardhu Isya empat rakaat menghadap kiblat karena Allah Ta'ala. (Sebagai makmum / Sebagai imam).",
            note = "Dimulai sejak hilangnya mega merah di ufuk barat hingga terbitnya fajar shadiq."
        ),
        PrayerGuideItem(
            id = "niat_jumat",
            category = GuideCategory.NIAT,
            title = "Niat Sholat Jum'at (2 Rakaat)",
            arabic = "أُصَلِّيْ فَرْضَ الْجُمُعَةِ رَكْعَتَيْنِ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً مَأْمُوْمًا لِلّٰهِ تَعَالَى",
            latin = "Ushollii fardhal jum'ati rak'ataini mustaqbilal qiblati adaa-an ma'muuman lillaahi ta'aala.",
            translation = "Saya berniat sholat fardhu Jum'at dua rakaat menghadap kiblat tunai sebagai makmum karena Allah Ta'ala.",
            note = "Wajib bagi laki-laki muslim pada waktu Dzuhur hari Jum'at didahului dua khutbah."
        ),
        PrayerGuideItem(
            id = "niat_dhuha",
            category = GuideCategory.NIAT,
            title = "Niat Sholat Dhuha (2 Rakaat)",
            arabic = "أُصَلِّيْ سُنَّةَ الضُّحَى رَكْعَتَيْنِ لِلّٰهِ تَعَالَى",
            latin = "Ushollii sunnatadh-dhuhaa rak'ataini lillaahi ta'aala.",
            translation = "Saya berniat sholat sunnah Dhuha dua rakaat karena Allah Ta'ala.",
            note = "Dikerjakan saat matahari naik setinggi tombak (sekitar pukul 07.00) hingga sebelum waktu Dzuhur. Keutamaannya sebagai sedekah seluruh persendian dan pembuka pintu rezeki."
        ),
        PrayerGuideItem(
            id = "niat_tahajjud",
            category = GuideCategory.NIAT,
            title = "Niat Sholat Tahajjud (2 Rakaat)",
            arabic = "أُصَلِّيْ سُنَّةَ التَّهَجُّدِ رَكْعَتَيْنِ لِلّٰهِ تَعَالَى",
            latin = "Ushollii sunnatat-tahajjudi rak'ataini lillaahi ta'aala.",
            translation = "Saya berniat sholat sunnah Tahajjud dua rakaat karena Allah Ta'ala.",
            note = "Dikerjakan pada malam hari sesudah tidur (terutama sepertiga malam terakhir). Keutamaannya diangkat ke derajat yang terpuji (maqamam mahmuda)."
        ),
        PrayerGuideItem(
            id = "niat_witir",
            category = GuideCategory.NIAT,
            title = "Niat Sholat Witir (1 / 3 Rakaat)",
            arabic = "أُصَلِّيْ سُنَّةَ الْوِتْرِ رَكْعَتَيْنِ (رَكْعَةً) لِلّٰهِ تَعَالَى",
            latin = "Ushollii sunnatal witri rak'ataini (rak'atan) lillaahi ta'aala.",
            translation = "Saya berniat sholat sunnah Witir dua rakaat (satu rakaat) karena Allah Ta'ala.",
            note = "Sholat penutup malam dengan bilangan rakaat ganjil (1, 3, 5 rakaat)."
        ),
        PrayerGuideItem(
            id = "niat_rawatib_qobliyah",
            category = GuideCategory.NIAT,
            title = "Niat Sholat Rawatib Qobliyah (Sebelum Sholat Fardhu)",
            arabic = "أُصَلِّيْ سُنَّةَ الظُّهْرِ (الصُّبْحِ) رَكْعَتَيْنِ قَبْلِيَّةً لِلّٰهِ تَعَالَى",
            latin = "Ushollii sunnatadh-dhuhri (ash-shubhi) rak'ataini qabliyyatan lillaahi ta'aala.",
            translation = "Saya berniat sholat sunnah sebelum Dzuhur (Subuh) dua rakaat karena Allah Ta'ala.",
            note = "Sunnah muakkadah yang sangat dianjurkan untuk menyempurnakan sholat fardhu."
        ),
        PrayerGuideItem(
            id = "niat_rawatib_badiyah",
            category = GuideCategory.NIAT,
            title = "Niat Sholat Rawatib Ba'diyah (Sesudah Sholat Fardhu)",
            arabic = "أُصَلِّيْ سُنَّةَ الظُّهْرِ (الْمَغْرِبِ / الْعِشَاءِ) رَكْعَتَيْنِ بَعْدِيَّةً لِلّٰهِ تَعَالَى",
            latin = "Ushollii sunnatadh-dhuhri (al-maghribi / al-'isyaa-i) rak'ataini ba'diyyatan lillaahi ta'aala.",
            translation = "Saya berniat sholat sunnah sesudah Dzuhur (Maghrib / Isya) dua rakaat karena Allah Ta'ala.",
            note = "Dikerjakan tepat setelah selesai sholat fardhu dan dzikir."
        ),
        PrayerGuideItem(
            id = "sholat_qunut_subuh",
            category = GuideCategory.NIAT,
            title = "Doa Qunut Sholat Subuh",
            arabic = "اَللّٰهُمَّ اهْدِنِيْ فِيْمَنْ هَدَيْتَ، وَعَافِنِيْ فِيْمَنْ عَافَيْتَ، وَتَوَلَّنِيْ فِيْمَنْ تَوَلَّيْتَ، وَبَارِكْ لِيْ فِيْمَا أَعْطَيْتَ، وَقِنِيْ شَرَّ مَا قَضَيْتَ، فَإِنَّكَ تَقْضِيْ وَلَا يُقْضَى عَلَيْكَ، وَإِنَّهُ لَا يَذِلُّ مَنْ وَالَيْتَ، وَلَا يَعِزُّ مَنْ عَادَيْتَ، تَبَارَكْتَ رَبَّنَا وَتَعَالَيْتَ، فَلَكَ الْحَمْدُ عَلَى مَا قَضَيْتَ، وَأَسْتَغْفِرُكَ وَأَتُوْبُ إِلَيْكَ، وَصَلَّى اللّٰهُ عَلَى سَيِّدِنَا مُحَمَّدٍ النَّبِيِّ الْأُمِّيِّ وَعَلَى آلِهِ وَصَحْبِهِ وَسَلَّمَ",
            latin = "Allaahummahdinii fii man hadait, wa 'aafinii fii man 'aafait, wa tawallanii fii man tawallait, wa baarik lii fii maa a'thait, wa qinii syarra maa qadhait, fa innaka taqdhii wa laa yuqdhaa 'alaik, wa innahu laa yadzillu maw-waalait, wa laa ya'izzu man 'aadait, tabaarakta rabbanaa wa ta'aalait, falakal hamdu 'alaa maa qadhait, wa astaghfiruka wa atuubu ilaik, wa shallallaahu 'alaa sayyidinaa Muhammadin-nabiyyil ummiyyi wa 'alaa aalihii wa shahbihii wa sallam.",
            translation = "Ya Allah, berilah aku petunjuk sebagaimana orang-orang yang telah Engkau beri petunjuk. Berilah aku kesehatan sebagaimana orang-orang yang telah Engkau beri kesehatan. Peliharalah aku sebagaimana orang-orang yang telah Engkau pelihara. Berkahilah bagiku apa yang telah Engkau berikan. Lindungilah aku dari keburukan yang telah Engkau tetapkan. Karena sesungguhnya Engkaulah yang menetapkan dan tidak ada yang menetapkan atas-Mu. Sesungguhnya tidak akan terhina orang yang Engkau cintai, dan tidak akan mulia orang yang Engkau musuhi. Maha Berkah Engkau wahai Tuhan kami dan Maha Tinggi. Bagi-Mu lah segala puji atas apa yang telah Engkau putuskan. Aku memohon ampun kepada-Mu dan bertaubat kepada-Mu. Dan semoga Allah melimpahkan shalawat serta salam kepada junjungan kami Nabi Muhammad yang ummi, beserta keluarga dan para sahabatnya.",
            note = "Sunnah ab'adh dalam mazhab Syafi'i yang dibaca saat I'tidal pada rakaat kedua sholat Subuh seraya mengangkat kedua tangan."
        ),

        // === 3. BACAAN SHOLAT LENGKAP ===
        PrayerGuideItem(
            id = "sholat_takbir",
            category = GuideCategory.BACAAN,
            title = "Takbiratul Ihram (Pembuka Sholat)",
            arabic = "اللّٰهُ أَكْبَرُ",
            latin = "Allaahu Akbar.",
            translation = "Allah Maha Besar.",
            note = "Rukun sholat: Mengangkat kedua tangan sejajar telinga/bahu seraya melafadzkan takbir dan menghadirkan niat di dalam hati.",
            stepNumber = 1
        ),
        PrayerGuideItem(
            id = "sholat_iftitah",
            category = GuideCategory.BACAAN,
            title = "Doa Iftitah (Sunnah)",
            arabic = "اللّٰهُ أَكْبَرُ كَبِيْرًا وَالْحَمْدُ لِلّٰهِ كَثِيْرًا وَسُبْحَانَ اللّٰهِ بُكْرَةً وَأَصِيْلًا، إِنِّيْ وَجَّهْتُ وَجْهِيَ لِلَّذِيْ فَطَرَ السَّمَاوَاتِ وَالْأَرْضَ حَنِيْفًا مُسْلِمًا وَمَا أَنَا مِنَ الْمُشْرِكِيْنَ، إِنَّ صَلَاتِيْ وَنُسُكِيْ وَمَحْيَايَ وَمَمَاتِيْ لِلّٰهِ رَبِّ الْعَالَمِيْنَ، لَا شَرِيْكَ لَهُ وَبِذٰلِكَ أُمِرْتُ وَأَنَا مِنَ الْمُسْلِمِيْنَ",
            latin = "Allaahu akbaru kabiiraa walhamdu lillaahi katsiiraa wa subhaanallaahi bukrataw-wa ashiilaa. Innii wajjahtu wajhiya lilladzii fatharas-samaawaati wal ardha haniifam-muslimaw-wamaa ana minal musyrikiin. Inna shalaatii wa nusukii wa mahyaaya wa mamaatii lillaahi rabbil 'aalamiin. Laa syariika lahu wa bidzaalika umirtu wa ana minal muslimiin.",
            translation = "Allah Maha Besar dengan sebesar-besarnya, segala puji bagi Allah sebanyak-banyaknya, dan Maha Suci Allah pada waktu pagi dan petang. Sesungguhnya aku hadapkan wajahku kepada Pencipta langit dan bumi dalam keadaan lurus dan berserah diri, dan bukanlah aku termasuk orang-orang musyrik. Sesungguhnya sholatku, ibadahku, hidupku dan matiku hanyalah untuk Allah Tuhan semesta alam. Tiada sekutu bagi-Nya dan demikianlah aku diperintahkan dan aku termasuk orang-orang yang berserah diri.",
            note = "Dibaca pelan setelah Takbiratul Ihram sebelum membaca Al-Fatihah pada rakaat pertama.",
            stepNumber = 2
        ),
        PrayerGuideItem(
            id = "sholat_fatihah",
            category = GuideCategory.BACAAN,
            title = "Surat Al-Fatihah (Rukun Wajib)",
            arabic = "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ (١) اَلْحَمْدُ لِلّٰهِ رَبِّ الْعَالَمِيْنَ (٢) الرَّحْمٰنِ الرَّحِيْمِ (٣) مَالِكِ يَوْمِ الدِّيْنِ (٤) إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِيْنُ (٥) اِهْدِنَا الصِّرَاطَ الْمُسْتَقِيْمَ (٦) صِرَاطَ الَّذِيْنَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوْبِ عَلَيْهِمْ وَلَا الضَّالِّيْنَ (٧)",
            latin = "Bismillaahir-rahmaanir-rahiim. Alhamdulillaahi rabbil 'aalamiin. Ar-rahmaanir-rahiim. Maaliki yawmid-diin. Iyyaaka na'budu wa iyyaaka nasta'iin. Ihdinash-shiraathal mustaqiim. Shiraathal ladziina an'amta 'alaihim ghairil maghdhuubi 'alaihim waladh-dhaalliin. (Aamiin).",
            translation = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang. Segala puji bagi Allah, Tuhan semesta alam. Maha Pengasih, Maha Penyayang. Pemilik hari pembalasan. Hanya kepada Engkaulah kami menyembah dan hanya kepada Engkaulah kami memohon pertolongan. Bimbinglah kami ke jalan yang lurus. (Yaitu) jalan orang-orang yang telah Engkau beri nikmat; bukan (jalan) mereka yang dimurkai dan bukan (pula jalan) mereka yang sesat. (Kabulkanlah doa kami ya Allah).",
            note = "Rukun sholat pada setiap rakaat.",
            stepNumber = 3
        ),
        PrayerGuideItem(
            id = "sholat_ruku",
            category = GuideCategory.BACAAN,
            title = "Bacaan Ruku' (3x)",
            arabic = "سُبْحَانَ رَبِّيَ الْعَظِيْمِ وَبِحَمْدِهِ",
            latin = "Subhaana rabbiyal 'azhiimi wa bihamdih. (3x)",
            translation = "Maha Suci Tuhanku Yang Maha Agung dan segala puji bagi-Nya.",
            note = "Punggung lurus mendatar, kedua tangan mencengkeram lutut dengan jari merenggang.",
            stepNumber = 4
        ),
        PrayerGuideItem(
            id = "sholat_itidal",
            category = GuideCategory.BACAAN,
            title = "I'tidal & Bangkit dari Ruku'",
            arabic = "سَمِعَ اللّٰهُ لِمَنْ حَمِدَهُ\nرَبَّنَا لَكَ الْحَمْدُ مِلْءَ السَّمَاوَاتِ وَمِلْءَ الْأَرْضِ وَمِلْءَ مَا شِئْتَ مِنْ شَيْءٍ بَعْدُ",
            latin = "Sami'allaahu liman hamidah. Rabbanaa lakal hamdu mil-us-samaawaati wa mil-ul ardhi wa mil-u maa syi'ta min syai-in ba'du.",
            translation = "Allah Maha Mendengar siapa yang memuji-Nya. Wahai Tuhan kami, bagi-Mu lah segala puji sepenuh langit, sepenuh bumi, dan sepenuh apa yang Engkau kehendaki sesudah itu.",
            note = "Bangkit tegak berdiri (thuma'ninah) dengan kedua tangan diangkat lalu diluruskan ke bawah.",
            stepNumber = 5
        ),
        PrayerGuideItem(
            id = "sholat_sujud",
            category = GuideCategory.BACAAN,
            title = "Bacaan Sujud (3x)",
            arabic = "سُبْحَانَ رَبِّيَ الْأَعْلَى وَبِحَمْدِهِ",
            latin = "Subhaana rabbiyal a'laa wa bihamdih. (3x)",
            translation = "Maha Suci Tuhanku Yang Maha Tinggi dan segala puji bagi-Nya.",
            note = "7 anggota badan menempel ke lantai: dahi dan hidung, kedua telapak tangan, kedua lutut, dan ujung jari jemari kedua kaki.",
            stepNumber = 6
        ),
        PrayerGuideItem(
            id = "sholat_duduk_antara_sujud",
            category = GuideCategory.BACAAN,
            title = "Duduk Antara Dua Sujud",
            arabic = "رَبِّ اغْفِرْ لِيْ وَارْحَمْنِيْ وَاجْبُرْنِيْ وَارْفَعْنِيْ وَارْزُقْنِيْ وَاهْدِنِيْ وَعَافِنِيْ وَاعْفُ عَنِّيْ",
            latin = "Rabbighfir lii warhamnii wajburnii warfa'nii warzuqnii wahdinii wa 'aafinii wa'fu 'annii.",
            translation = "Ya Tuhanku, ampunilah aku, sayangilah aku, cukupkanlah kekuranganku, tinggikanlah derajatku, berilah aku rezeki, berilah aku petunjuk, sehatkanlah aku, dan maafkanlah kesalahanku.",
            note = "Duduk iftirasy: menduduki kaki kiri dan menegakkan telapak kaki kanan dengan jari menghadap kiblat.",
            stepNumber = 7
        ),
        PrayerGuideItem(
            id = "sholat_tasyahhud_awal",
            category = GuideCategory.BACAAN,
            title = "Tasyahhud Awal",
            arabic = "التَّحِيَّاتُ الْمُبَارَكَاتُ الصَّلَوَاتُ الطَّيِّبَاتُ لِلّٰهِ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللّٰهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللّٰهِ الصَّالِحِيْنَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللّٰهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا رَسُوْلُ اللّٰهِ، اَللّٰهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ",
            latin = "Attahiyyaatul mubaarakaatush-shalawaatuth-thayyibaatu lillaah. Assalaamu 'alaika ayyuhan-nabiyyu wa rahmatullaahi wa barakaatuh. Assalaamu 'alainaa wa 'alaa 'ibaadillaahish-shaalihiin. Asyhadu an laa ilaaha illallaah, wa asyhadu anna Muhammadar-rasuulullaah. Allaahumma shalli 'alaa sayyidinaa Muhammad.",
            translation = "Segala kehormatan, keberkahan, rahmat, dan kebaikan adalah milik Allah. Semoga keselamatan tercurah kepadamu wahai Nabi, beserta rahmat Allah dan berkah-Nya. Semoga keselamatan tercurah kepada kami dan kepada hamba-hamba Allah yang shalih. Aku bersaksi tiada Tuhan selain Allah dan aku bersaksi Muhammad adalah utusan Allah. Ya Allah, limpahkanlah shalawat kepada junjungan kami Nabi Muhammad.",
            note = "Dibaca pada rakaat kedua pada sholat 3 dan 4 rakaat. Mengacungkan jari telunjuk kanan saat membaca 'illallaah'.",
            stepNumber = 8
        ),
        PrayerGuideItem(
            id = "sholat_tasyahhud_akhir",
            category = GuideCategory.BACAAN,
            title = "Tasyahhud Akhir & Shalawat Ibrahimiyah",
            arabic = "التَّحِيَّاتُ الْمُبَارَكَاتُ الصَّلَوَاتُ الطَّيِّبَاتُ لِلّٰهِ... وَعَلَى آلِ سَيِّدِنَا مُحَمَّدٍ، كَمَا صَلَّيْتَ عَلَى سَيِّدِنَا إِبْرَاهِيْمَ وَعَلَى آلِ سَيِّدِنَا إِبْرَاهِيْمَ، وَبَارِكْ عَلَى سَيِّدِنَا مُحَمَّدٍ وَعَلَى آلِ سَيِّدِنَا مُحَمَّدٍ، كَمَا بَارَكْتَ عَلَى سَيِّدِنَا إِبْرَاهِيْمَ وَعَلَى آلِ سَيِّدِنَا إِبْرَاهِيْمَ، فِي الْعَالَمِيْنَ إِنَّكَ حَمِيْدٌ مَجِيْدٌ",
            latin = "Attahiyyaatul mubaarakaatu... wa 'alaa aali sayyidinaa Muhammad, kamaa shallaita 'alaa sayyidinaa Ibraahiima wa 'alaa aali sayyidinaa Ibraahiim, wa baarik 'alaa sayyidinaa Muhammadin wa 'alaa aali sayyidinaa Muhammad, kamaa baarakta 'alaa sayyidinaa Ibraahiima wa 'alaa aali sayyidinaa Ibraahiim, fil 'aalamiina innaka hamiidum-majiid.",
            translation = "...dan kepada keluarga junjungan kami Nabi Muhammad, sebagaimana Engkau telah melimpahkan shalawat kepada Nabi Ibrahim dan keluarganya. Dan berkahilah Nabi Muhammad dan keluarganya, sebagaimana Engkau telah memberkahi Nabi Ibrahim dan keluarganya di alam semesta. Sesungguhnya Engkau Maha Terpuji lagi Maha Mulia.",
            note = "Duduk tawarruk: memasukkan kaki kiri di bawah kaki kanan dan bokong menempel di lantai.",
            stepNumber = 9
        ),
        PrayerGuideItem(
            id = "sholat_doa_sebelum_salam",
            category = GuideCategory.BACAAN,
            title = "Doa Perlindungan Sebelum Salam (Sunnah)",
            arabic = "اَللّٰهُمَّ إِنِّيْ أَعُوْذُ بِكَ مِنْ عَذَابِ جَهَنَّمَ، وَمِنْ عَذَابِ الْقَبْرِ، وَمِنْ فِتْنَةِ الْمَحْيَا وَالْمَمَاتِ، وَمِنْ شَرِّ فِتْنَةِ الْمَسِيْحِ الدَّجَّالِ",
            latin = "Allaahumma innii a'uudzu bika min 'adzaabi jahannam, wa min 'adzaabil qabri, wa min fitnatil mahyaa wal mamaat, wa min syarri fitnatil masiihid-dajjaal.",
            translation = "Ya Allah, sesungguhnya aku berlindung kepada-Mu dari siksa neraka Jahannam, dari siksa kubur, dari fitnah kehidupan dan kematian, dan dari keburukan fitnah Al-Masih Ad-Dajjal.",
            note = "Sunnah muakkadah dibaca sebelum menolehkan kepala untuk salam (HR. Bukhari & Muslim).",
            stepNumber = 10
        ),
        PrayerGuideItem(
            id = "sholat_salam",
            category = GuideCategory.BACAAN,
            title = "Salam (Penutup Sholat)",
            arabic = "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللّٰهِ وَبَرَكَاتُهُ",
            latin = "Assalaamu 'alaikum wa rahmatullaahi wa barakaatuh.",
            translation = "Semoga keselamatan, rahmat Allah, dan berkah-Nya tercurah kepada kalian.",
            note = "Rukun sholat: Menoleh ke kanan hingga pipi terlihat dari belakang, lalu menoleh ke kiri.",
            stepNumber = 11
        ),

        // === 4. DZIKIR & DOA SESUDAH SHOLAT ===
        PrayerGuideItem(
            id = "dzikir_istighfar",
            category = GuideCategory.DZIKIR_DOA,
            title = "Istighfar 3x & Doa Keselamatan",
            arabic = "أَسْتَغْفِرُ اللّٰهَ الْعَظِيْمَ (٣×)\nاَللّٰهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ السَّلَامُ تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ",
            latin = "Astaghfirullaahal 'azhiim (3x). Allaahumma antas-salaamu wa minkas-salaamu tabaarakta yaa dzal jalaali wal ikraam.",
            translation = "Aku memohon ampun kepada Allah Yang Maha Agung (3 kali). Ya Allah, Engkaulah Maha Damai dan dari-Mulah keselamatan, Maha Berkah Engkau wahai Pemilik Keagungan dan Kemuliaan.",
            note = "Dibaca langsung sesudah selesai sholat fardhu."
        ),
        PrayerGuideItem(
            id = "dzikir_tasbih_tahmid_takbir",
            category = GuideCategory.DZIKIR_DOA,
            title = "Tasbih, Tahmid, & Takbir (Masing-masing 33x)",
            arabic = "سُبْحَانَ اللّٰهِ (٣٣×)\nالْحَمْدُ لِلّٰهِ (٣٣×)\nاللّٰهُ أَكْبَرُ (٣٣×)\nلَا إِلٰهَ إِلَّا اللّٰهُ وَحْدَهُ لَا شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ يُحْيِيْ وَيُمِيْتُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيْرٌ",
            latin = "Subhaanallaah (33x). Alhamdulillaah (33x). Allaahu Akbar (33x). Laa ilaaha illallaahu wahdahu laa syariika lah, lahul mulku wa lahul hamdu yuhyii wa yumiitu wa huwa 'alaa kulli syai-in qadiir.",
            translation = "Maha Suci Allah (33x). Segala puji bagi Allah (33x). Allah Maha Besar (33x). Tiada Tuhan selain Allah Yang Maha Esa tiada sekutu bagi-Nya. Milik-Nya lah segala kerajaan dan milik-Nya segala pujian. Dia Maha Menghidupkan dan Mematikan, dan Dia Maha Kuasa atas segala sesuatu.",
            note = "Dzikir sunnah penutup sholat yang menghapuskan dosa walaupun sebanyak buih di lautan (HR. Muslim)."
        ),
        PrayerGuideItem(
            id = "dzikir_ayat_kursi",
            category = GuideCategory.DZIKIR_DOA,
            title = "Ayat Kursi (Surah Al-Baqarah: 255)",
            arabic = "اللّٰهُ لَا إِلٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّوْمُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِيْ يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيْهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيْطُوْنَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُوْدُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيْمُ",
            latin = "Allaahu laa ilaaha illaa huwal hayyul qayyuum, laa ta'khudzuhu sinatuw-wa laa nawm, lahu maa fis-samaawaati wa maa fil ardh, man dzal-ladzii yasyfa'u 'indahuu illaa bi-idznih, ya'lamu maa baina aidiihim wa maa khalfahum, wa laa yuhiithuuna bi syai-im min 'ilmihii illaa bimaa syaa', wasi'a kursiyyuhus-samaawaati wal ardh, wa laa ya-uuduhuu hifzhuhumaa, wa huwal 'aliyyul 'azhiim.",
            translation = "Allah, tidak ada tuhan selain Dia. Yang Maha Hidup, yang terus-menerus mengurus (makhluk-Nya), tidak mengantuk dan tidak tidur. Milik-Nya apa yang ada di langit dan apa yang ada di bumi. Tidak ada yang dapat memberi syafaat di sisi-Nya tanpa izin-Nya. Dia mengetahui apa yang ada di hadapan mereka dan apa yang ada di belakang mereka, dan mereka tidak mengetahui sesuatu apa pun tentang ilmu-Nya melainkan apa yang Dia kehendaki. Kursi-Nya (ilmu dan kekuasaan-Nya) meliputi langit dan bumi. Dan Dia tidak merasa berat memelihara keduanya, dan Dia Maha Tinggi, Maha Besar.",
            note = "Siapa yang membacanya sehabis sholat fardhu, tiada yang menghalanginya masuk surga selain kematian (HR. An-Nasa'i)."
        ),
        PrayerGuideItem(
            id = "doa_setelah_sholat",
            category = GuideCategory.DZIKIR_DOA,
            title = "Doa Ringkas & Mustajab Sesudah Sholat Fardhu",
            arabic = "الْحَمْدُ لِلّٰهِ رَبِّ الْعَالَمِيْنَ حَمْدًا يُوَافِيْ نِعَمَهُ وَيُكَافِئُ مَزِيْدَهُ، يَا رَبَّنَا لَكَ الْحَمْدُ كَمَا يَنْبَغِيْ لِجَلَالِ وَجْهِكَ وَلِعَظِيْمِ سُلْطَانِكَ، اَللّٰهُمَّ صَلِّ وَسَلِّمْ عَلَى سَيِّدِنَا مُحَمَّدٍ وَعَلَى آلِ سَيِّدِنَا مُحَمَّدٍ، رَبَّنَا اغْفِرْ لَنَا وَلِوَالِدِيْنَا وَارْحَمْهُمْ كَمَا رَبَّوْنَا صِغَارًا، رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ، وَصَلَّى اللّٰهُ عَلَى سَيِّدِنَا مُحَمَّدٍ وَعَلَى آلِهِ وَصَحْبِهِ وَسَلَّمَ، وَالْحَمْدُ لِلّٰهِ رَبِّ الْعَالَمِيْنَ",
            latin = "Alhamdulillaahi rabbil 'aalamiin hamday-yuwaafii ni'amahu wa yukaafi-u maziidah. Yaa rabbanaa lakal hamdu kamaa yambaghii lijalaali wajhika wa li'azhiimi sulthaanik. Allaahumma shalli wa sallim 'alaa sayyidinaa Muhammadin wa 'alaa aali sayyidinaa Muhammad. Rabbanaghfir lanaa wa liwaalidiinaa warhamhum kamaa rabbawnaa shighaaraa. Rabbanaa aatinaa fid-dunyaa hasanatan wa fil aakhirati hasanatan wa qinaa 'adzaaban-naar. Wa shallallaahu 'alaa sayyidinaa Muhammadin wa 'alaa aalihi wa shahbihii wa sallam, walhamdu lillaahi rabbil 'aalamiin.",
            translation = "Segala puji bagi Allah Tuhan semesta alam, pujian yang sebanding dengan nikmat-nikmat-Nya dan menjamin tambahannya. Wahai Tuhan kami, bagi-Mu segala puji sebagaimana pantas bagi keagungan Zat-Mu dan kebesaran kekuasaan-Mu. Ya Allah limpahkanlah shalawat dan salam kepada Nabi Muhammad dan keluarganya. Wahai Tuhan kami, ampunilah dosa kami dan dosa kedua orang tua kami, sayangilah mereka sebagaimana mereka mendidik kami semasa kecil. Wahai Tuhan kami, berikanlah kepada kami kebaikan di dunia dan kebaikan di akhirat, dan lindungilah kami dari siksa api neraka. Segala puji bagi Allah Tuhan semesta alam.",
            note = "Doa yang mengumpulkan permohonan ampunan, bakti orang tua, dan keselamatan dunia akhirat."
        )
    )

    fun getByCategory(category: GuideCategory): List<PrayerGuideItem> {
        if (category == GuideCategory.ALL) return items
        return items.filter { it.category == category }
    }

    fun search(query: String): List<PrayerGuideItem> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return items
        return items.filter {
            it.title.lowercase().contains(q) ||
            it.latin.lowercase().contains(q) ||
            it.translation.lowercase().contains(q) ||
            (it.note?.lowercase()?.contains(q) == true)
        }
    }
}

