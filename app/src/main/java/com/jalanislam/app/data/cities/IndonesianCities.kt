package com.jalanislam.app.data.cities

import com.jalanislam.app.data.model.CityLocation

object IndonesianCities {

    val CITIES = listOf(
        CityLocation("jkt", "Jakarta Pusat", "DKI Jakarta", -6.1818, 106.8223, "Asia/Jakarta"),
        CityLocation("sby", "Surabaya", "Jawa Timur", -7.2575, 112.7521, "Asia/Jakarta"),
        CityLocation("bdg", "Bandung", "Jawa Barat", -6.9175, 107.6191, "Asia/Jakarta"),
        CityLocation("mdn", "Medan", "Sumatera Utara", 3.5952, 98.6722, "Asia/Jakarta"),
        CityLocation("smg", "Semarang", "Jawa Tengah", -6.9667, 110.4167, "Asia/Jakarta"),
        CityLocation("mks", "Makassar", "Sulawesi Selatan", -5.1477, 119.4327, "Asia/Makassar"),
        CityLocation("plb", "Palembang", "Sumatera Selatan", -2.9761, 104.7754, "Asia/Jakarta"),
        CityLocation("tng", "Tangerang", "Banten", -6.1783, 106.6319, "Asia/Jakarta"),
        CityLocation("dpk", "Depok", "Jawa Barat", -6.4025, 106.7942, "Asia/Jakarta"),
        CityLocation("bks", "Bekasi", "Jawa Barat", -6.2383, 106.9756, "Asia/Jakarta"),
        CityLocation("bgr", "Bogor", "Jawa Barat", -6.5971, 106.8060, "Asia/Jakarta"),
        CityLocation("yk", "Yogyakarta", "D.I. Yogyakarta", -7.7956, 110.3695, "Asia/Jakarta"),
        CityLocation("slo", "Surakarta (Solo)", "Jawa Tengah", -7.5755, 110.8243, "Asia/Jakarta"),
        CityLocation("mlg", "Malang", "Jawa Timur", -7.9666, 112.6326, "Asia/Jakarta"),
        CityLocation("dps", "Denpasar", "Bali", -8.6705, 115.2126, "Asia/Makassar"),
        CityLocation("ach", "Banda Aceh", "Aceh", 5.5483, 95.3238, "Asia/Jakarta"),
        CityLocation("pdg", "Padang", "Sumatera Barat", -0.9471, 100.4172, "Asia/Jakarta"),
        CityLocation("pbr", "Pekanbaru", "Riau", 0.5071, 101.4478, "Asia/Jakarta"),
        CityLocation("btm", "Batam", "Kepulauan Riau", 1.1301, 104.0529, "Asia/Jakarta"),
        CityLocation("jmb", "Jambi", "Jambi", -1.6101, 103.6131, "Asia/Jakarta"),
        CityLocation("bkl", "Bengkulu", "Bengkulu", -3.8004, 102.2655, "Asia/Jakarta"),
        CityLocation("bdl", "Bandar Lampung", "Lampung", -5.3971, 105.2668, "Asia/Jakarta"),
        CityLocation("ptk", "Pontianak", "Kalimantan Barat", -0.0263, 109.3425, "Asia/Pontianak"),
        CityLocation("bpp", "Balikpapan", "Kalimantan Timur", -1.2379, 116.8529, "Asia/Makassar"),
        CityLocation("smd", "Samarinda", "Kalimantan Timur", -0.5022, 117.1536, "Asia/Makassar"),
        CityLocation("bjm", "Banjarmasin", "Kalimantan Selatan", -3.3194, 114.5908, "Asia/Makassar"),
        CityLocation("plk", "Palangkaraya", "Kalimantan Tengah", -2.2161, 113.9139, "Asia/Jakarta"),
        CityLocation("mnd", "Manado", "Sulawesi Utara", 1.4748, 124.8421, "Asia/Makassar"),
        CityLocation("plu", "Palu", "Sulawesi Tengah", -0.9003, 119.8779, "Asia/Makassar"),
        CityLocation("kdr", "Kendari", "Sulawesi Tenggara", -3.9985, 122.5126, "Asia/Makassar"),
        CityLocation("gtr", "Gorontalo", "Gorontalo", 0.5435, 123.0568, "Asia/Makassar"),
        CityLocation("mam", "Mamuju", "Sulawesi Barat", -2.6770, 118.8895, "Asia/Makassar"),
        CityLocation("mtr", "Mataram", "Nusa Tenggara Barat", -8.5833, 116.1167, "Asia/Makassar"),
        CityLocation("kpg", "Kupang", "Nusa Tenggara Timur", -10.1772, 123.6070, "Asia/Makassar"),
        CityLocation("abn", "Ambon", "Maluku", -3.6554, 128.1908, "Asia/Jayapura"),
        CityLocation("tte", "Ternate", "Maluku Utara", 0.7906, 127.3837, "Asia/Jayapura"),
        CityLocation("jyp", "Jayapura", "Papua", -2.5916, 140.6690, "Asia/Jayapura"),
        CityLocation("srn", "Sorong", "Papua Barat Daya", -0.8762, 131.2558, "Asia/Jayapura"),
        CityLocation("mrk", "Merauke", "Papua Selatan", -8.4991, 140.4049, "Asia/Jayapura")
    )

    fun getDefaultCity(): CityLocation = CITIES[0] // Jakarta Pusat

    fun findById(id: String): CityLocation? = CITIES.find { it.id == id }

    fun search(query: String): List<CityLocation> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return CITIES
        return CITIES.filter {
            it.name.lowercase().contains(q) || it.province.lowercase().contains(q)
        }
    }
}

