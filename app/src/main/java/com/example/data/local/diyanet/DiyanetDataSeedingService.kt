package com.example.data.local.diyanet

import android.util.Log
import com.example.data.local.AppDatabase
import com.example.data.model.CategoryType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Diyanet veritabanı yapısına uygun olarak Tefsir, Hadis ve Fıkıh külliyatlarını
 * ve ilgili temel İslami eserleri kategorilerine göre Room veritabanına kaydeden
 * otomatik veri doldurma (seeding) servisi.
 */
object DiyanetDataSeedingService {

  private const val TAG = "DiyanetSeedingService"

  suspend fun seedIfNeeded(database: AppDatabase) = withContext(Dispatchers.IO) {
    val dao = database.diyanetDao()
    val existingCount = dao.getBookCount()
    if (existingCount == 0) {
      Log.i(TAG, "Diyanet veritabanı boş bulundu. Külliyatlar kaydediliyor...")
      populateAllCollections(dao)
      Log.i(TAG, "Diyanet veritabanı doldurma işlemi başarıyla tamamlandı.")
    } else {
      Log.i(TAG, "Diyanet veritabanında $existingCount adet eser mevcut. Doldurma atlandı.")
    }
  }

  suspend fun forceReseed(database: AppDatabase) = withContext(Dispatchers.IO) {
    val dao = database.diyanetDao()
    populateAllCollections(dao)
  }

  private suspend fun populateAllCollections(dao: DiyanetDao) {
    val books = getInitialBooks()
    val chapters = getInitialChapters()
    val terms = getInitialTerms()

    dao.refreshDiyanetDatabase(books, chapters, terms)
  }

  // =========================================================================
  // 1. KİTAP ENTITY LİSTESİ (TEFSİR, HADİS, FIKIH VE DİĞER KÜLLİYATLAR)
  // =========================================================================
  fun getInitialBooks(): List<DiyanetBookEntity> = listOf(
    // --- TEFSİR KÜLLİYATI ---
    DiyanetBookEntity(
      id = "tefsir_diyanet_kuran_yolu",
      title = "Kur'an Yolu Meal ve Tefsiri (Diyanet)",
      author = "Diyanet İşleri Başkanlığı (Heyet: Hayrettin Karaman vd.)",
      publisher = "Diyanet İşleri Başkanlığı Yayınları",
      category = CategoryType.TEFSIR.name,
      discipline = "Kur'an Meali ve Tefsir Usulü",
      description = "Diyanet İşleri Başkanlığı tarafından alanında uzman tefsir uleması heyetince hazırlanan 5 ciltlik Türkçe meal ve tefsir külliyatı.",
      totalVolumes = 5,
      publicationYear = 2020,
      isbn = "978-975-19-4080-4",
      isOfficialDiyanet = true
    ),
    DiyanetBookEntity(
      id = "tefsir_elmali_hak_dini",
      title = "Hak Dini Kur'an Dili (Elmalılı Tefsiri)",
      author = "Elmalılı Muhammed Hamdi Yazır",
      publisher = "Diyanet İşleri Reisliği / DİB Neşriyatı",
      category = CategoryType.TEFSIR.name,
      discipline = "Klasik Türkçe Dirayet Tefsiri",
      description = "Cumhuriyet döneminde Diyanet İşleri Reisliği'nin talebiyle Elmalılı M. Hamdi Yazır tarafından kaleme alınan 9 ciltlik abidevi tefsir şaheseri.",
      totalVolumes = 9,
      publicationYear = 1935,
      isbn = "978-975-19-1234-5",
      isOfficialDiyanet = true
    ),
    DiyanetBookEntity(
      id = "tefsir_fizilalil_kuran",
      title = "Fî Zılâli'l-Kur'ân (Kur'an'ın Gölgesinde)",
      author = "Seyyid Kutub",
      publisher = "İslami İlimler Kütüphanesi",
      category = CategoryType.TEFSIR.name,
      discipline = "Edebi ve Harekî Tefsir",
      description = "Kur'an-ı Kerim'in diriltici mesajını ve tevhid nizamını çağdaş insanın idrakine sunan edebi ve fikri tefsir külliyatı.",
      totalVolumes = 6,
      publicationYear = 1965,
      isbn = "978-975-456-112-3",
      isOfficialDiyanet = false
    ),

    // --- HADİS KÜLLİYATI ---
    DiyanetBookEntity(
      id = "hadis_diyanet_hadislerle_islam",
      title = "Hadislerle İslam (Diyanet Külliyatı)",
      author = "Diyanet İşleri Başkanlığı Yayınları (Komisyon)",
      publisher = "Diyanet İşleri Başkanlığı Yayınları",
      category = CategoryType.HADIS.name,
      discipline = "Konulu Hadis Projesi",
      description = "Hz. Peygamber'in çağlar üstü mesajını günümüz insanına ulaştırmak üzere Diyanet tarafından hazırlanan 7 ciltlik dev hadis külliyatı.",
      totalVolumes = 7,
      publicationYear = 2013,
      isbn = "978-975-19-5566-2",
      isOfficialDiyanet = true
    ),
    DiyanetBookEntity(
      id = "hadis_kutub_sitte",
      title = "Kütüb-i Sitte Mecmuası",
      author = "Buhârî, Müslim, Ebû Dâvûd, Tirmizî, Nesâî, İbn Mâce",
      publisher = "Dârü'l-Kütüb ve's-Sünne",
      category = CategoryType.HADIS.name,
      discipline = "Sahih ve Sünen Hadis Kaynakları",
      description = "İslam ümmetinin en muteber kabul ettiği altı temel sahih hadis mecmuasının en sahih rivayetlerini ihtiva eden külliyat.",
      totalVolumes = 6,
      publicationYear = 2000,
      isbn = "978-975-345-098-1",
      isOfficialDiyanet = false
    ),
    DiyanetBookEntity(
      id = "hadis_riyazus_salihin",
      title = "Riyâzü's-Sâlihîn (Salihlerin Bahçesi)",
      author = "İmam Ebû Zekeriyyâ en-Nevevî (DİB Neşri)",
      publisher = "Diyanet İşleri Başkanlığı Yayınları",
      category = CategoryType.HADIS.name,
      discipline = "Ahlak ve İbadet Hadisleri",
      description = "İmam Nevevî'nin müminin günlük ibadet, ahlak ve muamelatında rehber kıldığı, Diyanet tarafından tercüme ve şerh edilen temel hadis klasiği.",
      totalVolumes = 3,
      publicationYear = 2017,
      isbn = "978-975-19-6789-0",
      isOfficialDiyanet = true
    ),

    // --- FIKIH KÜLLİYATI ---
    DiyanetBookEntity(
      id = "fikih_diyanet_ilmihal",
      title = "İslam İlmihali (Diyanet)",
      author = "Diyanet İşleri Başkanlığı (Lütfi Şentürk, Seyfettin Yazıcı)",
      publisher = "Diyanet İşleri Başkanlığı Yayınları",
      category = CategoryType.FIKIH.name,
      discipline = "Temel İslam Fıkhı ve İlmihali",
      description = "Müslümanın ibadet, taharet, namaz, oruç, zekat ve helal-haram konularındaki dini mükellefiyetlerini Hanefi fıkhı esasında açıklayan resmî ilmihal.",
      totalVolumes = 2,
      publicationYear = 2021,
      isbn = "978-975-19-0123-8",
      isOfficialDiyanet = true
    ),
    DiyanetBookEntity(
      id = "fikih_diyanet_fetvalar",
      title = "Din İşleri Yüksek Kurulu Fetvaları",
      author = "Diyanet İşleri Başkanlığı Din İşleri Yüksek Kurulu",
      publisher = "Diyanet İşleri Başkanlığı Yayınları",
      category = CategoryType.FIKIH.name,
      discipline = "Güncel Fıkhi Meseleler ve Fetvalar",
      description = "Diyanet'in en üst dini danışma ve karar organı olan Din İşleri Yüksek Kurulu tarafından verilen güncel ibadet, sağlık, ticaret ve aile fetvaları.",
      totalVolumes = 2,
      publicationYear = 2022,
      isbn = "978-975-19-7234-1",
      isOfficialDiyanet = true
    ),
    DiyanetBookEntity(
      id = "fikih_fetavayi_hindiye",
      title = "Fetâvâ-yı Hindiyye (el-Fetâva'l-Hindiyye)",
      author = "Şeyh Nizam & Hint Uleması Heyeti (Sultan Evrengzib)",
      publisher = "Klasik Fıkıh Neşriyatı",
      category = CategoryType.FIKIH.name,
      discipline = "Genişletilmiş Hanefi Fetva Külliyatı",
      description = "Babür İmparatoru Sultan Evrengzib'in emriyle 500'ü aşkın İslam fakihi tarafından hazırlanan, Osmanlı mahkemelerinde de uygulanan dev fetva külliyatı.",
      totalVolumes = 6,
      publicationYear = 1890,
      isbn = "978-975-567-890-2",
      isOfficialDiyanet = false
    ),

    // --- SİYER KÜLLİYATI ---
    DiyanetBookEntity(
      id = "siyer_diyanet_saricam",
      title = "Hz. Muhammed ve Evrensel Mesajı (Diyanet)",
      author = "Prof. Dr. İbrahim Sarıçam (Diyanet İşleri Başkanlığı)",
      publisher = "Diyanet İşleri Başkanlığı Yayınları",
      category = CategoryType.SIYER.name,
      discipline = "Akademik Siyer-i Nebi",
      description = "Hz. Peygamber'in hayatını, tebliğ mücadelesini ve Medine medeniyeti inşasını güvenilir kaynaklara dayanarak anlatan Diyanet eseri.",
      totalVolumes = 1,
      publicationYear = 2019,
      isbn = "978-975-19-3456-7",
      isOfficialDiyanet = true
    ),
    DiyanetBookEntity(
      id = "siyer_asim_koksal",
      title = "İslam Tarihi (Peygamberimiz ve Ashabı)",
      author = "M. Âsım Köksal",
      publisher = "Köksal Neşriyat",
      category = CategoryType.SIYER.name,
      discipline = "Tafsilatlı Siyer Ansiklopedisi",
      description = "Siret-i Nebeviye sahasında İslam dünyasında birincilik ödülü kazanan 18 ciltlik muazzam kaynak eser.",
      totalVolumes = 8,
      publicationYear = 1985,
      isbn = "978-975-789-012-3",
      isOfficialDiyanet = false
    ),

    // --- İSLAM TARİHİ KÜLLİYATI ---
    DiyanetBookEntity(
      id = "tarih_diyanet_islam_medeniyeti",
      title = "İslam Tarihi ve Medeniyeti (Diyanet / TDV)",
      author = "Diyanet İşleri Başkanlığı & TDV İslami Araştırmalar Heyeti",
      publisher = "Diyanet Vakfı Yayınları",
      category = CategoryType.ISLAM_TARIHI.name,
      discipline = "İslam ve Medeniyet Tarihi",
      description = "Dört Halife devrinden Selçuklu ve Osmanlı vakıf medeniyetine kadar İslam dünyasının tarihi yürüyüşünü ele alan ansiklopedik eser.",
      totalVolumes = 4,
      publicationYear = 2021,
      isbn = "978-975-389-990-1",
      isOfficialDiyanet = true
    ),
    DiyanetBookEntity(
      id = "tarih_ibnul_esir",
      title = "el-Kâmil fi't-Târîh (İbnü'l-Esîr Tarihi)",
      author = "İzzüddin İbnü'l-Esîr el-Cezerî",
      publisher = "Tarih Külliyatı",
      category = CategoryType.ISLAM_TARIHI.name,
      discipline = "Külli İslam Tarihi",
      description = "İslam tarihçiliğinin zirve kaynaklarından biri kabul edilen, hilafet ve medeniyet devirlerini ayrıntılarıyla aktaran 12 ciltlik şaheser.",
      totalVolumes = 12,
      publicationYear = 1230,
      isbn = "978-975-678-123-4",
      isOfficialDiyanet = false
    ),

    // --- AKAİD KÜLLİYATI ---
    DiyanetBookEntity(
      id = "akaid_diyanet_inanc_esaslari",
      title = "İslam İnanç Esasları (Akaid - Diyanet)",
      author = "Diyanet İşleri Başkanlığı (Din İşleri Yüksek Kurulu Komisyonu)",
      publisher = "Diyanet İşleri Başkanlığı Yayınları",
      category = CategoryType.AKAID.name,
      discipline = "Ehl-i Sünnet Akaidi",
      description = "İslam inanç esaslarını, Allah'ın sıfatlarını, nübüvveti ve ahiret inancını aklı ve nakli delillerle izah eden Diyanet'in temel inanç rehberi.",
      totalVolumes = 1,
      publicationYear = 2020,
      isbn = "978-975-19-6112-9",
      isOfficialDiyanet = true
    ),
    DiyanetBookEntity(
      id = "akaid_nesefiyye",
      title = "Akaidü'n-Nesefî (Metin ve Şerh)",
      author = "Necmeddin Ömer en-Nesefî",
      publisher = "Kelam ve Akaid Kütüphanesi",
      category = CategoryType.AKAID.name,
      discipline = "Klasik Kelam Metni",
      description = "Maturidiyye ve Eş'ariyye Ehl-i Sünnet çizgisinin asırlardır medreselerde okutulan en muhtasar ve veciz inanç metni.",
      totalVolumes = 1,
      publicationYear = 1142,
      isbn = "978-975-890-456-7",
      isOfficialDiyanet = false
    ),

    // --- AHLAK KÜLLİYATI ---
    DiyanetBookEntity(
      id = "ahlak_diyanet_islam_ahlaki",
      title = "İslam Ahlakı ve Yaşayan Değerlerimiz (Diyanet)",
      author = "Prof. Dr. Mustafa Çağrıcı (Diyanet İşleri Başkanlığı)",
      publisher = "Diyanet İşleri Başkanlığı Yayınları",
      category = CategoryType.AHLAK.name,
      discipline = "İslam Ahlak Felsefesi ve Pratiği",
      description = "Kur'an ve Sünnet ışığında bireysel ve toplumsal ahlak ilkelerini, erdemleri ve gönül terbiyesini inceleyen muteber Diyanet yayını.",
      totalVolumes = 1,
      publicationYear = 2018,
      isbn = "978-975-19-5432-0",
      isOfficialDiyanet = true
    ),
    DiyanetBookEntity(
      id = "ahlak_ihyau_ulumiddin",
      title = "İhyâu Ulûmi'd-Dîn (Din İlimlerinin İhyası)",
      author = "Hüccetü'l-İslam İmam Gazâlî",
      publisher = "Tasavvuf ve Ahlak Külliyatı",
      category = CategoryType.AHLAK.name,
      discipline = "Kalp İlimleri ve Tasavvufi Ahlak",
      description = "İslam düşünce tarihinin en mühim ahlak ve nefis terbiyesi külliyatı. İbadetlerin batıni sırları ve kalbi helak eden afetleri konu edinir.",
      totalVolumes = 4,
      publicationYear = 1097,
      isbn = "978-975-234-567-8",
      isOfficialDiyanet = false
    )
  )

  // =========================================================================
  // 2. FASILLAR / BÖLÜMLER (TEFSİR, HADİS, FIKIH VE DİĞER BRANŞLAR)
  // =========================================================================
  fun getInitialChapters(): List<DiyanetChapterEntity> = listOf(
    // -------------------------------------------------------------
    // TEFSİR 1: Kur'an Yolu (Diyanet)
    // -------------------------------------------------------------
    DiyanetChapterEntity(
      id = "diyanet_kuran_yolu_ch_1",
      bookId = "tefsir_diyanet_kuran_yolu",
      chapterIndex = 0,
      topicTitle = "1. Konu: Fâtiha Sûresi ve İstiane Tefsiri",
      subTopic = "Hamd, Rahmân, Rahîm ve Sırat-ı Müstakîm",
      arabicTitle = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: FÂTİHA SÛRESİ VE İSTİANE TEFSİRİ

Diyanet Kur'an Yolu Tefsiri'nden:
'Fâtiha, Kur'an-ı Kerim'in mukaddimesi ve anahtarıdır. Mushaf'ın başında yer aldığı için bu adı almıştır. Yüce Allah bu sûrede kullarına Kendisini tanıtır; Hamd'in yalnızca Âlemlerin Rabbi olan Allah'a mahsus olduğunu, O'nun sonsuz merhamet sahibi (Rahmân ve Rahîm) ve ceza/mükafat gününün yegane mâliki olduğunu bildirir.'

'Yalnız Sana kulluk eder, yalnız Senden yardım dileriz' âyeti:
İnsanın kulluk borcunu yalnızca Yaratıcısına hasretmesini, kula kulluktan ve her türlü şirkten kurtularak hürriyete kavuşmasını ifade eder. Bizi dosdoğru yola (sırat-ı müstakîm) ilet duası ise peygamberlerin, sıddıkların ve şehitlerin izinde yürümektir.""",
      pageNumber = 1,
      sourceReference = "Kur'an Yolu Tefsiri, Cilt 1, Fâtiha Sûresi 1-7",
      category = CategoryType.TEFSIR.name,
      orderIndex = 1
    ),
    DiyanetChapterEntity(
      id = "diyanet_kuran_yolu_ch_2",
      bookId = "tefsir_diyanet_kuran_yolu",
      chapterIndex = 1,
      topicTitle = "2. Konu: Bakara Sûresi ve Âyetü'l-Kürsî Hikmeti",
      subTopic = "Tevhid Sırları, Hayy ve Kayyûm İsimleri",
      arabicTitle = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: BAKARA SÛRESİ VE ÂYETÜ'L-KÜRSÎ HİKMETİ

Diyanet Kur'an Yolu İzahı (Bakara 255):
'Âyetü'l-Kürsî, tevhid inancının en kapsamlı ve en veciz ifadesidir. Yüce Allah'ın Hayy (ezeli ve ebedi diri) ve Kayyûm (bütün varlıkları ayakta tutan) sıfatları zikredilir. O'nu ne bir gaflet, ne bir uyuklama ne de bir uyku tutar.
Kürsî kavramı; Allah'ın sonsuz ilmini, kudretini ve kainat üzerindeki mutlak hakimiyetini temsil eder. Göklerde ve yerde ne varsa O'nun izni olmadan hiçbir kimse şefaat edemez. Bu âyet müminin kalbine mutlak bir emniyet ve huzur aşılar.'""",
      pageNumber = 2,
      sourceReference = "Bakara Sûresi 255. Âyet",
      category = CategoryType.TEFSIR.name,
      orderIndex = 2
    ),
    DiyanetChapterEntity(
      id = "diyanet_kuran_yolu_ch_3",
      bookId = "tefsir_diyanet_kuran_yolu",
      chapterIndex = 2,
      topicTitle = "3. Konu: Yâsîn ve Mülk Sûreleri Tefsiri",
      subTopic = "Ölüm, Hayat İmtihanı ve Diriliş",
      arabicTitle = "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
3. KONU: YÂSÎN VE MÜLK SÛRELERİ TEFSİRİ

Kur'an Yolu Tefsirinden Notlar:
Mülk Sûresi 2. Âyet: 'O, hanginizin daha güzel amel yapacağını sınamak için ölümü ve hayatı yaratandır.'
Ölüm bir yok oluş değil, ebedi ahiret hayatına açılan bir kapıdır. Hayat ise insanın ilahi rızayı kazanmak üzere tabi tutulduğu bir imtihan meydanıdır.
Yâsîn Sûresi ise kainattaki ilahi kudret akışını, yeşil ağaçtan çıkan ateşi, dönen gök cisimlerini ve öldükten sonra yeniden dirilmeyi (ba's) apaçık delillerle akıllara ve vicdanlara sunar.""",
      pageNumber = 3,
      sourceReference = "Mülk Sûresi 1-5; Yâsîn Sûresi 77-83",
      category = CategoryType.TEFSIR.name,
      orderIndex = 3
    ),

    // -------------------------------------------------------------
    // TEFSİR 2: Elmalılı Hak Dini Kur'an Dili
    // -------------------------------------------------------------
    DiyanetChapterEntity(
      id = "elmali_hak_dini_ch_1",
      bookId = "tefsir_elmali_hak_dini",
      chapterIndex = 0,
      topicTitle = "1. Konu: Fâtiha-i Şerîfe ve Besmele İzahı",
      subTopic = "Ümmü'l-Kitâb ve İbadetin Gayesi",
      arabicTitle = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: FÂTİHA-İ ŞERÎFE VE BESMELE İZAHI

Elmalılı Merhum tefsirine şöyle başlar:
'Hamd olsun o Hâlık-ı Hakîm'e ki, kâinatı hikmetle donatmış ve insan nev'ini ahsen-i takvîm üzere halkedip lütuf ve keremiyle akıl ve beyan nimetiyle mükerrem kılmıştır. Besmele, her hayrın miftahı ve her ilahi bereketin membaıdır.'
Fâtiha, Kur'an'ın ihtiva ettiği dört ana maksadı (Tevhid, Nübüvvet, Âhiret ve Adalet/İbadet) bünyesinde cem etmiştir.""",
      pageNumber = 1,
      sourceReference = "Hak Dini Kur'an Dili, Cilt 1",
      category = CategoryType.TEFSIR.name,
      orderIndex = 4
    ),

    // -------------------------------------------------------------
    // HADİS 1: Hadislerle İslam (Diyanet)
    // -------------------------------------------------------------
    DiyanetChapterEntity(
      id = "diyanet_hadislerle_islam_ch_1",
      bookId = "hadis_diyanet_hadislerle_islam",
      chapterIndex = 0,
      topicTitle = "1. Konu: İman ve İhlas (Cilt 1 - Kalbin Samimiyeti)",
      subTopic = "Din Nasihattir / Samimiyettir Hadisi",
      arabicTitle = "الدِّينُ النَّصِيحَةُ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: İMAN VE İHLAS (CİLT 1 - KALBİN SAMİMİYETİ)

Diyanet Hadislerle İslam Külliyatından:
Resûlullah (s.a.v.) buyurdu: 'Din samimiyettir (nasihattir).'
Ashab sordu: 'Kime karşı ey Allah'ın Resûlü?'
Efendimiz buyurdu: 'Allah'a, Kitabı'na, Resûlü'ne, Müslümanların yöneticilerine ve bütün Müslümanlara karşı samimi olmaktır.' (Müslim, Îmân 95)

Şerh ve Mesaj:
İslam, kalbin niyet ve samimiyet üzerinde yükseldiği bir hayat nizamıdır. İbadetlerde gösterişten uzak durmak, insanlarla ilişkilerde hile ve ikiyüzlülükten sakınmak imanın en temel göstergesidir.""",
      pageNumber = 1,
      sourceReference = "Hadislerle İslam, Cilt 1, Sahife 45",
      category = CategoryType.HADIS.name,
      orderIndex = 5
    ),
    DiyanetChapterEntity(
      id = "diyanet_hadislerle_islam_ch_2",
      bookId = "hadis_diyanet_hadislerle_islam",
      chapterIndex = 1,
      topicTitle = "2. Konu: İbadet ve Dua Şuuru (Cilt 2 - Kulluk Coşkusu)",
      subTopic = "Secde Hali ve Dua İbadetin Özüdür",
      arabicTitle = "أَقْرَبُ مَا يَكُونُ الْعَبْدُ مِنْ رَبِّهِ وَهُوَ سَاجِدٌ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: İBADET VE DUA ŞUURU (CİLT 2 - KULLUK COŞKUSU)

Hadis-i Şerif Şerhi:
Resûl-i Ekrem (s.a.v.) şöyle buyurmuştur: 'Kulun Rabbine en yakın olduğu an, secde anıdır. Öyleyse secdede çokça dua ediniz.' (Müslim, Salât 215)
Dua, kulun aczini itiraf edip Sonsuz Kudret Sahibi'ne yönelmesidir. Peygamberimiz duayı 'ibadetin özü ve beyni' olarak nitelendirmiştir. Namaz ise müminin miracı, günde beş vakit manevi arınma ırmağıdır.""",
      pageNumber = 2,
      sourceReference = "Hadislerle İslam, Cilt 2, Dua Bahsi",
      category = CategoryType.HADIS.name,
      orderIndex = 6
    ),
    DiyanetChapterEntity(
      id = "diyanet_hadislerle_islam_ch_3",
      bookId = "hadis_diyanet_hadislerle_islam",
      chapterIndex = 2,
      topicTitle = "3. Konu: Güzel Ahlak ve Merhamet (Cilt 3 - İnsani Değerler)",
      subTopic = "Müminin Ahlakı ve Merhamet İlkesi",
      arabicTitle = "إِنَّمَا بُعِثْتُ لِأُتَمِّمَ صَالِحَ الْأَخْلَاقِ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
3. KONU: GÜZEL AHLAK VE MERHAMET (CİLT 3 - İNSANİ DEĞERLER)

Peygamberimiz buyurdu: 'Müminlerin iman bakımından en mükemmeli, ahlakı en güzel olanıdır. Sizin en hayırlınız da ailesine ve eşine karşı en hayırlı olanınızdır.' (Tirmizî, Radâ 11)
'Merhamet etmeyene merhamet olunmaz' ilkesi, İslam'ın kainata ve bütün canlılara bakışının özüdür.""",
      pageNumber = 3,
      sourceReference = "Hadislerle İslam, Cilt 3, Ahlak Bölümü",
      category = CategoryType.HADIS.name,
      orderIndex = 7
    ),

    // -------------------------------------------------------------
    // FIKIH 1: İslam İlmihali (Diyanet)
    // -------------------------------------------------------------
    DiyanetChapterEntity(
      id = "diyanet_ilmihal_ch_1",
      bookId = "fikih_diyanet_ilmihal",
      chapterIndex = 0,
      topicTitle = "1. Konu: İbadet ve Temizlik (Tahâret Ahkâmı)",
      subTopic = "Abdest, Gusül ve Necasetten Temizlik",
      arabicTitle = "الطَّهُورُ شَطْرُ الإِيمَانِ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: İBADET VE TEMİZLİK (TAHÂRET AHKÂMI)

Diyanet İslam İlmihali'nden:
İbadet, kulun Yaratıcısına olan sevgi, saygı, itaat ve şükrünü ifade eden fiillerdir. İslam dininde namaz gibi bedenî ibadetlerin geçerli olabilmesi için maddi ve manevi pisliklerden arınmış olmak şarttır.
Hades ve Necasetten Taharet:
- Abdestin farzları: Yüzü yıkamak, elleri dirseklerle beraber yıkamak, başın dörtte birini meshetmek ve ayakları topuklarla yıkamaktır.
- Guslün farzları: Ağza su vermek (madmada), burna su vermek (istinşak) ve bütün bedeni kuru yer kalmayacak şekilde yıkamaktır.""",
      pageNumber = 1,
      sourceReference = "Diyanet İslam İlmihali, Cilt 1, Taharet Bahsi",
      category = CategoryType.FIKIH.name,
      orderIndex = 8
    ),
    DiyanetChapterEntity(
      id = "diyanet_ilmihal_ch_2",
      bookId = "fikih_diyanet_ilmihal",
      chapterIndex = 1,
      topicTitle = "2. Konu: Namaz Kitabı ve Şartları",
      subTopic = "Namazın Şartları, Rükünleri ve İmamet",
      arabicTitle = "الصَّلَاةُ عِمَادُ الدِّينِ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: NAMAZ KİTABI VE ŞARTLARI

Diyanet İlmihali Namaz Bölümü:
Namaz, akıl baliğ olan her Müslümana farz-ı ayndır. Dinin direğidir.
Namazın Dışındaki Şartları (6 Şart):
1. Hadesten taharet (Abdest/Gusül)
2. Necasetten taharet (Elbise ve mekan temizliği)
3. Setr-i avret (Örtünme)
4. İstikbâl-i kıble (Kâbe'ye yönelmek)
5. Vakit (Namaz vaktinin girmesi)
6. Niyet.
Namazın İçindeki Farzları (6 Rükün):
İftitah tekbiri, Kıyam, Kıraat, Rükû, Sücûd ve Ka'de-i ahîre (Son oturuş).""",
      pageNumber = 2,
      sourceReference = "Diyanet İslam İlmihali, Namaz Bölümü",
      category = CategoryType.FIKIH.name,
      orderIndex = 9
    ),
    DiyanetChapterEntity(
      id = "diyanet_ilmihal_ch_3",
      bookId = "fikih_diyanet_ilmihal",
      chapterIndex = 2,
      topicTitle = "3. Konu: Zekat ve Oruç İbadeti",
      subTopic = "Nisap Miktarları ve Mükellefiyet",
      arabicTitle = "وَأَقِيمُوا الصَّلَاةَ وَآتُوا الزَّكَاةَ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
3. KONU: ZEKAT VE ORUÇ İBADETİ

Zekat: Dinen zengin sayılan (nisap miktarı mala sahip olan ve borçları düşüldükten sonra üzerinden bir yıl geçen) Müslümanların mallarından ihtiyaç sahiplerine vermeleri gereken kırkta bir (%2.5) farz hissedir.
Oruç: İmsak vaktinden akşam güneş batışına kadar yeme, içme ve nefsani arzulardan ibadet niyetiyle uzak durmaktır.""",
      pageNumber = 3,
      sourceReference = "Diyanet İslam İlmihali, Zekat & Oruç",
      category = CategoryType.FIKIH.name,
      orderIndex = 10
    ),

    // -------------------------------------------------------------
    // FIKIH 2: Din İşleri Yüksek Kurulu Fetvaları
    // -------------------------------------------------------------
    DiyanetChapterEntity(
      id = "diyanet_fetva_ch_1",
      bookId = "fikih_diyanet_fetvalar",
      chapterIndex = 0,
      topicTitle = "1. Konu: Güncel İbadet ve Sağlık Fetvaları",
      subTopic = "İğne, Aşı, Tedavi ve Seferilik Hükümleri",
      arabicTitle = "فَاسْأَلُوا أَهْلَ الذِّكْرِ إِن كُنتُمْ لَا تَعْلَمُونَ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: GÜNCEL İBADET VE SAĞLIK FETVALARI

Din İşleri Yüksek Kurulu Kararlarından Seçkiler:
- İlaç ve İğne Orucu Bozar mı?
Besin veya vitamin değeri taşımayan, sadece tedavi amaçlı yapılan iğneler ve aşılar orucu bozmaz. Ancak gıda ve serum niteliğindeki enjeksiyonlar orucu bozar, kaza gerektirir.
- Seferilik ve Namazların Kısaltılması:
Meşru bir gaye ile en az 90 km mesafeye yolculuğa çıkan kimse, gittiği yerde 15 günden az kalacaksa dört rekatlı farz namazları iki rekat olarak kılar.""",
      pageNumber = 1,
      sourceReference = "Diyanet Din İşleri Yüksek Kurulu Fetva Arşivi",
      category = CategoryType.FIKIH.name,
      orderIndex = 11
    ),
    DiyanetChapterEntity(
      id = "diyanet_fetva_ch_2",
      bookId = "fikih_diyanet_fetvalar",
      chapterIndex = 1,
      topicTitle = "2. Konu: Ticaret, Faiz ve Dijital Muamelat Fetvaları",
      subTopic = "Helal Kazanç ve Finansal İlkeler",
      arabicTitle = "وَأَحَلَّ اللَّهُ الْبَيْعَ وَحَرَّمَ الرِّبَا",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: TİCARET, FAİZ VE DİJİTAL MUAMELAT FETVALARI

Diyanet Fetvalarından:
- Faiz ve Riba Yasağı:
Her türlü faizli muamele İslam dininde kesin olarak haram kılınmıştır. Karşılıksız fazlalık şartı taşıyan akidler batıldır.
- Kripto Paralar ve Dijital Varlıklar:
Devlet güvencesi bulunmayan, spekülatif, haksız zenginleşmeye ve kara para aklamaya müsait belirsiz işlemler dinen caiz görülmemektedir.""",
      pageNumber = 2,
      sourceReference = "DİB Fetvalar, İktisadi Hayat",
      category = CategoryType.FIKIH.name,
      orderIndex = 12
    ),

    // -------------------------------------------------------------
    // SİYER 1: Hz. Muhammed ve Evrensel Mesajı (Diyanet)
    // -------------------------------------------------------------
    DiyanetChapterEntity(
      id = "diyanet_siyer_ch_1",
      bookId = "siyer_diyanet_saricam",
      chapterIndex = 0,
      topicTitle = "1. Konu: Risalet Öncesi Mekke ve Hilfü'l-Fudûl",
      subTopic = "Muhammedü'l-Emîn ve Erdemliler Hareketi",
      arabicTitle = "لَقَدْ جَاءَكُمْ رَسُولٌ مِّنْ أَنفُسِكُمْ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: RİSALET ÖNCESİ MEKKE VE HİLFÜ'L-FUDÛL

Diyanet Siyer Külliyatı'ndan:
Hz. Muhammed (s.a.v.), gençliğinde haksızlığa uğrayan mazlum bir tüccarın hakkını savunmak üzere kurulan 'Hilfü'l-Fudûl' cemiyetine bizzat katılmıştır. Peygamberimiz: 'İslam'da da böyle bir cemiyete çağrılsam tereddüt etmeden icabet ederim' buyurarak adaletin evrensel bir değer olduğunu ilan etmiştir.""",
      pageNumber = 1,
      sourceReference = "Prof. Dr. İbrahim Sarıçam, DİB Yayınları",
      category = CategoryType.SIYER.name,
      orderIndex = 13
    ),

    // -------------------------------------------------------------
    // İSLAM TARİHİ 1: İslam Tarihi ve Medeniyeti (Diyanet / TDV)
    // -------------------------------------------------------------
    DiyanetChapterEntity(
      id = "diyanet_tarih_ch_1",
      bookId = "tarih_diyanet_islam_medeniyeti",
      chapterIndex = 0,
      topicTitle = "1. Konu: Dört Halife Devri ve Adalet Çağı (Hulefâ-i Râşidîn)",
      subTopic = "Şûra, Adalet ve Divan Teşkilatı",
      arabicTitle = "وَأَمْرُهُمْ شُورَىٰ بَيْنَهُمْ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: DÖRT HALİFE DEVRİ VE ADALET ÇAĞI (HULEFÂ-İ RÂŞİDÎN)

Peygamber Efendimiz'in vefatından sonra hilafet makamına geçen Hulefâ-i Râşidîn devri, İslam siyaset ahlakının zirvesidir.
Hz. Ebû Bekir devrinde Kur'an Mushaf haline getirilmiş; Hz. Ömer devrinde adliye teşkilatı ve beytülmal kurulmuştur.""",
      pageNumber = 1,
      sourceReference = "DİB / TDV İslam Tarihi Külliyatı",
      category = CategoryType.ISLAM_TARIHI.name,
      orderIndex = 14
    ),

    // -------------------------------------------------------------
    // AKAİD 1: İslam İnanç Esasları (Akaid - Diyanet)
    // -------------------------------------------------------------
    DiyanetChapterEntity(
      id = "diyanet_akaid_ch_1",
      bookId = "akaid_diyanet_inanc_esaslari",
      chapterIndex = 0,
      topicTitle = "1. Konu: Allah'a İman ve Esmâ-i Hüsnâ",
      subTopic = "Zâtî ve Sübûtî Sıfatlar",
      arabicTitle = "وَلِلَّهِ الْأَسْمَاءُ الْحُسْنَىٰ فَادْعُوهُ بِهَا",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: ALLAH'A İMAN VE ESMÂ-İ HÜSNÂ

Diyanet İslam İnanç Esasları Kitabından:
Akaid; İslam dininde kalben inanılması zaruri olan itikadi esasları inceler.
- Zâtî Sıfatlar: Vücud, Kıdem, Bekâ, Vahdaniyyet, Muhalefetün li'l-havâdis, Kıyam bi-nefsihî.
- Sübûtî Sıfatlar: Hayat, İlim, Sem', Basar, İrade, Kudret, Kelâm ve Tekvîn.""",
      pageNumber = 1,
      sourceReference = "DİB Din İşleri Yüksek Kurulu İnanç Kitabı",
      category = CategoryType.AKAID.name,
      orderIndex = 15
    ),

    // -------------------------------------------------------------
    // AHLAK 1: İslam Ahlakı ve Yaşayan Değerlerimiz (Diyanet)
    // -------------------------------------------------------------
    DiyanetChapterEntity(
      id = "diyanet_ahlak_ch_1",
      bookId = "ahlak_diyanet_islam_ahlaki",
      chapterIndex = 0,
      topicTitle = "1. Konu: İslam Ahlakının Temelleri ve İhlas",
      subTopic = "Haya, Samimiyet ve Sıdk",
      arabicTitle = "وَإِنَّكَ لَعَلَىٰ خُلُقٍ عَظِيمٍ",
      content = """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: İSLAM AHLAKININ TEMELLERİ VE İHLAS

Diyanet İslam Ahlakı Kitabından:
Ahlak, insanın yaratılışındaki güzelliği davranışlarına yansıtmasıdır. Kur'an-ı Kerim: 'Şüphesiz sen pek yüce bir ahlak üzeresin' (Kalem Sûresi 4) buyurmuştur.
İhlas ve Samimiyet: İslam ahlakında riyadan, gösterişten ve menfaatten uzak durmak esastır.""",
      pageNumber = 1,
      sourceReference = "Prof. Dr. Mustafa Çağrıcı, DİB Yayınları",
      category = CategoryType.AHLAK.name,
      orderIndex = 16
    )
  )

  // =========================================================================
  // 3. İSLAMİ LÜGAT & ISTILAH ENTITY LİSTESİ
  // =========================================================================
  fun getInitialTerms(): List<DiyanetTermEntity> = listOf(
    DiyanetTermEntity("Tevhid", "v-h-d (وَحَدَ)", "Allah Teâlâ'nın zâtında, sıfatlarında ve fiillerinde bir ve tek olduğuna inanmaktır.", CategoryType.AKAID.name),
    DiyanetTermEntity("İhlas", "h-l-s (خَلَصَ)", "İbadet ve fiillerde sırf Allah rızasını gözetmek, riyadan kalbi arındırmaktır.", CategoryType.AHLAK.name),
    DiyanetTermEntity("Takva", "v-k-y (وَقَى)", "Allah'ın emirlerine sarılıp yasaklarından kaçınarak ilahi azaptan korunmaktır.", CategoryType.AHLAK.name),
    DiyanetTermEntity("Fıkıh", "f-k-h (فَقِهَ)", "Tafsili şer'i delillerden çıkarılan ameli dini hükümleri bilme ilmidir.", CategoryType.FIKIH.name),
    DiyanetTermEntity("Tefsir", "f-s-r (فَسَرَ)", "Kur'an-ı Kerim'in lafız ve manalarını açıklayıp beyan eden ilim dalı.", CategoryType.TEFSIR.name),
    DiyanetTermEntity("Sünnet", "s-n-n (سَنَّ)", "Hz. Peygamber'in (s.a.v.) söz, fiil ve takrirleriyle ortaya koyduğu hayat tarzı.", CategoryType.HADIS.name),
    DiyanetTermEntity("Fetva", "f-t-y (فَتَى)", "Dini bir meselenin şer'i hükmünü açıklayan yetkili dini cevap.", CategoryType.FIKIH.name),
    DiyanetTermEntity("İcma", "c-m-a (جَمَعَ)", "İslam müctehidlerinin dini bir hüküm üzerinde ittifak etmeleridir.", CategoryType.FIKIH.name),
    DiyanetTermEntity("Kıyas", "k-y-s (قَاسَ)", "Hakkında nas bulunmayan meseleye benzer illet sebebiyle hüküm bağlamaktır.", CategoryType.FIKIH.name)
  )
}
