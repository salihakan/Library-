package com.example.data.repository

import com.example.data.model.Book
import com.example.data.model.CategoryType
import com.example.data.model.DictionaryTerm

object DefaultLibraryData {

  val islamicDictionary: Map<String, DictionaryTerm> = listOf(
    DictionaryTerm(
      term = "Tevhid",
      root = "v-h-d (وَحَدَ)",
      definition = "Allah Teâlâ'nın zâtında, sıfatlarında ve fiillerinde bir ve tek olduğuna, hiçbir eşi ve benzeri bulunmadığına kalben inanıp dille ikrar etmektir. İslam akaidinin temel direğidir.",
      category = "Akaid"
    ),
    DictionaryTerm(
      term = "İhlas",
      root = "h-l-s (خَلَصَ)",
      definition = "Söz, fiil ve ibadetlerde sırf Allah'ın rızasını gözetmek; riya, gösteriş ve dünyevi menfaat şaibelerinden kalbi arındırmaktır.",
      category = "Ahlak"
    ),
    DictionaryTerm(
      term = "Takva",
      root = "v-k-y (وَقَى)",
      definition = "Kulun, Allah'ın emirlerine sımsıkı sarılıp yasaklarından kaçınarak kendisini ilahi azaptan koruması ve daima haşyet içinde bulunmasıdır.",
      category = "Ahlak"
    ),
    DictionaryTerm(
      term = "Hadis",
      root = "h-d-s (حَدَثَ)",
      definition = "Hz. Peygamber'in (s.a.v.) sözleri, fiilleri, takrirleri (onayları) ve ahlaki veya fiziki vasıflarını ihtiva eden rivayetlerdir.",
      category = "Hadis"
    ),
    DictionaryTerm(
      term = "Sünnet",
      root = "s-n-n (سَنَّ)",
      definition = "Resûlullah'ın (s.a.v.) din adına ortaya koyduğu ve ashabınca nakledilen hayat tarzı, hükümler ve ameli prensipler bütünü.",
      category = "Hadis"
    ),
    DictionaryTerm(
      term = "İcma",
      root = "c-m-a (جَمَعَ)",
      definition = "Hz. Muhammed'in (s.a.v.) vefatından sonra herhangi bir asırda İslam müctehidlerinin dini bir hüküm üzerinde ittifak etmeleridir.",
      category = "Fıkıh"
    ),
    DictionaryTerm(
      term = "Kıyas",
      root = "k-y-s (قَاسَ)",
      definition = "Hakkında nas (ayet ve hadis) bulunmayan fıkhi bir meseleye, aralarındaki ortak illet sebebiyle hakkında nas bulunan meselenin hükmünü vermektir.",
      category = "Fıkıh"
    ),
    DictionaryTerm(
      term = "Fıkıh",
      root = "f-k-h (فَقِهَ)",
      definition = "Lugatte derinlemesine anlamak ve kavramaktır. Istılahta ise tafsili delillerden (kitap, sünnet, icma, kıyas) çıkarılan şer'i-ameli hükümleri bilmektir.",
      category = "Fıkıh"
    ),
    DictionaryTerm(
      term = "Tefsir",
      root = "f-s-r (فَسَرَ)",
      definition = "Kur'an-ı Kerim'in lafızlarını, i'rabını, nüzul sebeplerini, delalet ettiği manaları ve maksatlarını beşeri takat nispetinde açıklayıp beyan eden ilim dalı.",
      category = "Tefsir"
    ),
    DictionaryTerm(
      term = "Tecvid",
      root = "c-v-d (جَوَّدَ)",
      definition = "Kur'an harflerinin mahreçlerine (çıkış yerlerine) ve sıfatlarına riayet ederek, uzatma, tutma ve durağan kaideleriyle en güzel şekilde tilavet edilmesidir.",
      category = "Tefsir"
    ),
    DictionaryTerm(
      term = "Sahabe",
      root = "s-h-b (صَحِبَ)",
      definition = "Hz. Peygamber'i (s.a.v.) mümin olarak gören, onun sohbetinde bulunan ve İslam üzere vefat eden bahtiyar nesil.",
      category = "Siyer"
    ),
    DictionaryTerm(
      term = "Mütevatir",
      root = "v-t-r (وَتَرَ)",
      definition = "Yalan üzere ittifak etmeleri aklen imkansız olan kalabalık bir topluluğun her nesilde naklettiği kesin bilgi ifade eden haber veya hadis.",
      category = "Hadis"
    ),
    DictionaryTerm(
      term = "İctihad",
      root = "c-h-d (جَهَدَ)",
      definition = "Fakihin, şer'i delillerden zanni hükümleri çıkarmak için var gücüyle zihni ve ilmi gayret sarf etmesidir.",
      category = "Fıkıh"
    ),
    DictionaryTerm(
      term = "Farz",
      root = "f-r-d (فَرَضَ)",
      definition = "Şâri'in (Allah ve Resulü'nün) mükelleften yapılmasını kesin ve bağlayıcı bir şekilde istediği, inkarı küfrü gerektiren ameli hüküm (Namaza durmak, oruç tutmak gibi).",
      category = "Fıkıh"
    ),
    DictionaryTerm(
      term = "Vacip",
      root = "v-c-b (وَجَبَ)",
      definition = "Hanefi mezhebine göre sübutu veya delaleti zanni bir delille kesin olarak istenen hüküm (Vitir namazı, kurban kesmek gibi).",
      category = "Fıkıh"
    ),
    DictionaryTerm(
      term = "İhsan",
      root = "h-s-n (حَسُنَ)",
      definition = "Cibril hadisinde tarif edildiği üzere: 'Allah'ı görüyormuşçasına O'na ibadet etmendir; sen O'nu görmesen de O seni görmektedir.'",
      category = "Ahlak"
    ),
    DictionaryTerm(
      term = "Zühd",
      root = "z-h-d (زَهِدَ)",
      definition = "Dünyaya ve fani nimetlere kalben bağlanmamak, zaruri ihtiyaçlar dışında gereksiz lüksten sakınarak ahiret yurduna rağbet etmektir.",
      category = "Ahlak"
    ),
    DictionaryTerm(
      term = "Kelam",
      root = "k-l-m (كَلَمَ)",
      definition = "İslam inanç esaslarını aklı ve nakli delillerle ispat eden, şüpheleri izale edip inancı müdafaa eden ilim.",
      category = "Akaid"
    ),
    DictionaryTerm(
      term = "Nübüvvet",
      root = "n-b-e (نَبَأَ)",
      definition = "Allah Teâlâ'nın kulları arasından seçtiği elçilerine vahiy indirerek onları insanlara rehber kılması peygamberlik müessesesidir.",
      category = "Akaid"
    ),
    DictionaryTerm(
      term = "Siyer",
      root = "s-y-r (سَارَ)",
      definition = "Hz. Peygamber'in (s.a.v.) doğumundan vefatına kadar geçen mübarek hayatını, gazalarını ve ahlakını ele alan ilim.",
      category = "Siyer"
    ),
    DictionaryTerm(
      term = "Fetva",
      root = "f-t-y (فَتَى)",
      definition = "Dini bir meselenin şer'i hükmünü açıklamak üzere müftü veya fıkıh alimi tarafından verilen bağlayıcı olmayan dini cevap ve hüküm.",
      category = "Fıkıh"
    )
  ).associateBy { it.term.lowercase() }

  val defaultBooks: List<Book> = listOf(
    // =========================================================================
    // 1. TEFSİR (Diyanet & Klasik Eserler)
    // =========================================================================
    Book(
      id = "tefsir_diyanet_kuran_yolu",
      title = "Kur'an Yolu Meal ve Tefsiri (Diyanet)",
      author = "Diyanet İşleri Başkanlığı (Heyet: Hayrettin Karaman vd.)",
      category = CategoryType.TEFSIR,
      topic = "Diyanet Kur'an Yolu Tefsiri",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: FÂTİHA SÛRESİ VE İSTİANE TEFSİRİ

Diyanet Kur'an Yolu Tefsiri'nden:
'Fâtiha, Kur'an-ı Kerim'in mukaddimesi ve anahtarıdır. Mushaf'ın başında yer aldığı için bu adı almıştır. Yüce Allah bu sûrede kullarına Kendisini tanıtır; Hamd'in yalnızca Âlemlerin Rabbi olan Allah'a mahsus olduğunu, O'nun sonsuz merhamet sahibi (Rahmân ve Rahîm) ve ceza/mükafat gününün yegane mâliki olduğunu bildirir.'

'Yalnız Sana kulluk eder, yalnız Senden yardım dileriz' âyeti:
İnsanın kulluk borcunu yalnızca Yaratıcısına hasretmesini, kula kulluktan ve her türlü şirkten kurtularak hürriyete kavuşmasını ifade eder. Bizi dosdoğru yola (sırat-ı müstakîm) ilet duası ise peygamberlerin, sıddıkların ve şehitlerin izinde yürümektir.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: BAKARA SÛRESİ VE ÂYETÜ'L-KÜRSÎ HİKMETİ

Diyanet Kur'an Yolu İzahı (Bakara 255):
'Âyetü'l-Kürsî, tevhid inancının en kapsamlı ve en veciz ifadesidir. Yüce Allah'ın Hayy (ezeli ve ebedi diri) ve Kayyûm (bütün varlıkları ayakta tutan) sıfatları zikredilir. O'nu ne bir gaflet, ne bir uyuklama ne de bir uyku tutar.
Kürsî kavramı; Allah'ın sonsuz ilmini, kudretini ve kainat üzerindeki mutlak hakimiyetini temsil eder. Göklerde ve yerde ne varsa O'nun izni olmadan hiçbir kimse şefaat edemez. Bu âyet müminin kalbine mutlak bir emniyet ve huzur aşılar.'""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
3. KONU: YÂSÎN VE MÜLK SÛRELERİ TEFSİRİ

Kur'an Yolu Tefsirinden Notlar:
Mülk Sûresi 2. Âyet: 'O, hanginizin daha güzel amel yapacağını sınamak için ölümü ve hayatı yaratandır.'
Ölüm bir yok oluş değil, ebedi ahiret hayatına açılan bir kapıdır. Hayat ise insanın ilahi rızayı kazanmak üzere tabi tutulduğu bir imtihan meydanıdır.
Yâsîn Sûresi ise kainattaki ilahi kudret akışını, yeşil ağaçtan çıkan ateşi, dönen gök cisimlerini ve öldükten sonra yeniden dirilmeyi (ba's) apaçık delillerle akıllara ve vicdanlara sunar."""
      )
    ),

    Book(
      id = "tefsir_elmali_hak_dini",
      title = "Hak Dini Kur'an Dili (Elmalılı Tefsiri)",
      author = "Elmalılı Muhammed Hamdi Yazır",
      category = CategoryType.TEFSIR,
      topic = "Hak Dini Tefsiri & Ayet İzahları",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: FÂTİHA-İ ŞERÎFE VE BESMELE İZAHI

Elmalılı Merhum tefsirine şöyle başlar:
'Hamd olsun o Hâlık-ı Hakîm'e ki, kâinatı hikmetle donatmış ve insan nev'ini ahsen-i takvîm üzere halkedip lütuf ve keremiyle akıl ve beyan nimetiyle mükerrem kılmıştır. Besmele, her hayrın miftahı ve her ilahi bereketin membaıdır.'

Fâtiha Sûresi Tefsir Özeti:
Fâtiha, Kur'an-ı Kerim'in fihristesi mesabesindedir. Zira Kur'an'ın ihtiva ettiği dört ana maksadı (Tevhid, Nübüvvet, Âhiret ve Adalet/İbadet) bünyesinde cem etmiştir.
'İyyâke na'büdü ve iyyâke neste'în': Kulluk yalnız Sana mahsustur, yardım da ancak Senden dilenir.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: ÂYETÜ'L-KÜRSÎ VE TEVHİD SIRLARI (BAKARA 255)

'Allâhü lâ ilâhe illâ hüve'l-hayyü'l-kayyûm...'
Elmalılı M. Hamdi Yazır bu âyetin şerhinde buyurur:
'Âyetü'l-Kürsî, Kur'an âyetlerinin seyyidi ve tevhid ilminin şahikasıdır. Cenâb-ı Hakk'ın Hayy ve Kayyûm sıfatları burada tecelli eder. Kul bu hakikati tefekkür ettiğinde, kâinattaki hiçbir hadisenin tesadüf olmadığını idrak eder.'""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
3. KONU: İHLÂS VE MUAVVİZETEYN ŞERHİ

'Kul hüvellâhu ehad. Allâhü's-samed...'
Elmalılı Tefsirinden Notlar:
İhlâs sûresi, Cenâb-ı Hakk'ı tenzih sıfatlarıyla tanıtır. 'Ehad' lafzı bölünme ve cüz kabul etmeyen mutlak birliği; 'Samed' lafzı ise herkesin kendisine muhtaç olduğu, kendisinin ise hiçbir şeye muhtaç olmadığı zâtı ifade eder."""
      )
    ),

    Book(
      id = "tefsir_fizilalil_kuran",
      title = "Fî Zılâli'l-Kur'ân (Kur'an'ın Gölgesinde)",
      author = "Seyyid Kutub",
      category = CategoryType.TEFSIR,
      topic = "Fî Zılâli'l-Kur'ân Tefsiri & Yaşayan Kur'an",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: KUR'AN'IN GÖLGESİNDE HAYAT VE TEVHİD

Seyyid Kutub tefsirin mukaddimesinde şöyle haykırır:
'Kur'an'ın gölgesinde yaşamak öyle büyük bir nimettir ki, onu ancak o zevki tadanlar bilir. Hayatımda geçirdiğim en bereketli ve huzurlu anlar, Kur'an'ın diriltici ikliminde nefes aldığım demlerdir. Kur'an, tarihin tozlu raflarına hapsedilecek nazari bir kitap değil; yaşayan, harekete geçiren canlı bir hayat nizamıdır.'""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: ASR SÛRESİ VE İSLAMÎ KURTULUŞ NİZAMI

'Vel-asr. İnne'l-insâne lefî husr...'
Fî Zılâli'l-Kur'ân'dan İzah:
İmam Şâfiî'nin buyurduğu gibi: 'Şayet Kur'an'dan başka hiçbir sûre inmeseydi, şu küçücük Asr sûresi insanlığa hidayet rehberi olarak yeterdi.' İnsanlık ancak iman, salih amel, hakkı ve sabrı tavsiye ile kurtuluşa erer."""
      )
    ),

    // =========================================================================
    // 2. HADİS (Diyanet & Kütüb-i Sitte)
    // =========================================================================
    Book(
      id = "hadis_diyanet_hadislerle_islam",
      title = "Hadislerle İslam (Diyanet Külliyatı)",
      author = "Diyanet İşleri Başkanlığı Yayınları (Komisyon Heyeti)",
      category = CategoryType.HADIS,
      topic = "Diyanet Hadislerle İslam Külliyatı",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: İMAN VE İHLAS (CİLT 1 - KALBİN SAMİMİYETİ)

Diyanet Hadislerle İslam Külliyatından:
Resûlullah (s.a.v.) buyurdu: 'Din samimiyettir (nasihattir).'
Ashab sordu: 'Kime karşı ey Allah'ın Resûlü?'
Efendimiz buyurdu: 'Allah'a, Kitabı'na, Resûlü'ne, Müslümanların yöneticilerine ve bütün Müslümanlara karşı samimi olmaktır.' (Müslim, Îmân 95)

Şerh ve Mesaj:
İslam, kalbin niyet ve samimiyet üzerinde yükseldiği bir hayat nizamıdır. İbadetlerde gösterişten uzak durmak, insanlarla ilişkilerde hile ve ikiyüzlülükten sakınmak imanın en temel göstergesidir.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: İBADET VE DUA ŞUURU (CİLT 2 - KULLUK COŞKUSU)

Hadis-i Şerif Şerhi:
Resûl-i Ekrem (s.a.v.) şöyle buyurmuştur: 'Kulun Rabbine en yakın olduğu an, secde anıdır. Öyleyse secdede çokça dua ediniz.' (Müslim, Salât 215)
Dua, kulun aczini itiraf edip Sonsuz Kudret Sahibi'ne yönelmesidir. Peygamberimiz duayı 'ibadetin özü ve beyni' olarak nitelendirmiştir. Namaz ise müminin miracı, günde beş vakit manevi arınma ırmağıdır.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
3. KONU: GÜZEL AHLAK VE MERHAMET (CİLT 3 - İNSANİ DEĞERLER)

Hz. Âişe (r.a.) naklediyor: 'Resûlullah (s.a.v.) ahlakça insanların en güzeli idi.'
Peygamberimiz buyurdu: 'Müminlerin iman bakımından en mükemmeli, ahlakı en güzel olanıdır. Sizin en hayırlınız da ailesine ve eşine karşı en hayırlı olanınızdır.' (Tirmizî, Radâ 11)
Merhamet etmeyene merhamet olunmaz ilkesi, İslam'ın kainata ve bütün canlılara bakışının özüdür."""
      )
    ),

    Book(
      id = "hadis_kutub_sitte",
      title = "Kütüb-i Sitte Mecmuası",
      author = "Buhârî, Müslim, Ebû Dâvûd, Tirmizî, Nesâî, İbn Mâce",
      category = CategoryType.HADIS,
      topic = "Kütüb-i Sitte Hadisleri & Şerhleri",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: İMÂN, İHLÂS VE NİYET BAHİSLERİ (BUHÂRÎ 1)

Hz. Ömer b. Hattâb'dan (r.a.) rivayetle Resûlullah (s.a.v.) şöyle buyurdu:
'Ameller ancak niyetlere göredir. Herkes için niyet ettiği şey vardır. Kimin hicreti Allah'a ve Resûlü'ne ise, hicreti Allah ve Resûlü'nedir.' (Buhârî, Bed'ü'l-Vahy 1)""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: CİBRÎL HADİSİ: İSLÂM, İMÂN VE İHSÂN (MÜSLİM)

İhsan nedir sualine Efendimiz: 'Allah'ı görüyormuş gibi O'na kulluk etmendir. Zira sen O'nu görmesen de O seni görmektedir' buyurdu."""
      )
    ),

    Book(
      id = "hadis_riyazus_salihin",
      title = "Riyâzü's-Sâlihîn (Salihlerin Bahçesi)",
      author = "İmam Ebû Zekeriyyâ en-Nevevî (DİB Neşri)",
      category = CategoryType.HADIS,
      topic = "Riyâzü's-Sâlihîn & Sünnet-i Seniyye",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: BÂBÜ'L-İHLÂS VE İHZÂRİ'N-NİYYET (İHLAS BAHSI)

İmam Nevevî kitabına ihlas bahsiyle başlar:
Ebû Hüreyre'den rivayetle Resûlullah (s.a.v.) buyurdu: 'Şüphesiz ki Allah sizin suretlerinize ve mallarınıza bakmaz; fakat O sizin kalplerinize ve amellerinize bakar.' (Müslim, Birr 34)""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: BÂBÜ'T-TEVBE VE MAĞFİRET KAPISI

'Ey insanlar! Allah'a tevbe ediniz ve O'ndan bağışlanma dileyiniz. Vallahi ben günde yüz defa tevbe ve istiğfar ediyorum.' (Müslim, Zikir 41)"""
      )
    ),

    // =========================================================================
    // 3. FIKIH (Diyanet İlmihali & Din İşleri Yüksek Kurulu Fetvaları)
    // =========================================================================
    Book(
      id = "fikih_diyanet_ilmihal",
      title = "İslam İlmihali (Diyanet)",
      author = "Diyanet İşleri Başkanlığı (Lütfi Şentürk, Seyfettin Yazıcı)",
      category = CategoryType.FIKIH,
      topic = "Diyanet İslam İlmihali & İbadet Esasları",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: İBADET VE TEMİZLİK (TAHÂRET AHKÂMI)

Diyanet İslam İlmihali'nden:
İbadet, kulun Yaratıcısına olan sevgi, saygı, itaat ve şükrünü ifade eden fiillerdir. İslam dininde namaz gibi bedenî ibadetlerin geçerli olabilmesi için maddi ve manevi pisliklerden arınmış olmak şarttır.
Hades ve Necasetten Taharet:
- Abdestin farzları: Yüzü yıkamak, elleri dirseklerle beraber yıkamak, başın dörtte birini meshetmek ve ayakları topuklarla yıkamaktır.
- Guslün farzları: Ağza su vermek (madmada), burna su vermek (istinşak) ve bütün bedeni kuru yer kalmayacak şekilde yıkamaktır.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
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

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
3. KONU: ZEKAT VE ORUÇ İBADETİ

Zekat: Dinen zengin sayılan (nisap miktarı mala sahip olan ve borçları düşüldükten sonra üzerinden bir yıl geçen) Müslümanların mallarından ihtiyaç sahiplerine vermeleri gereken kırkta bir (%2.5) farz hissedir.
Oruç: İmsak vaktinden akşam güneş batışına kadar yeme, içme ve nefsani arzulardan ibadet niyetiyle uzak durmaktır."""
      )
    ),

    Book(
      id = "fikih_diyanet_fetvalar",
      title = "Din İşleri Yüksek Kurulu Fetvaları",
      author = "Diyanet İşleri Başkanlığı Din İşleri Yüksek Kurulu",
      category = CategoryType.FIKIH,
      topic = "Diyanet Din İşleri Yüksek Kurulu Fetvaları",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: GÜNCEL İBADET VE SAĞLIK FETVALARI

Din İşleri Yüksek Kurulu Kararlarından Seçkiler:
- İlaç ve İğne Orucu Bozar mı?
Besin veya vitamin değeri taşımayan, sadece tedavi amaçlı yapılan iğneler ve aşılar orucu bozmaz. Ancak gıda ve serum niteliğindeki enjeksiyonlar orucu bozar, kaza gerektirir.
- Seferilik ve Namazların Kısaltılması:
Meşru bir gaye ile en az 90 km mesafeye yolculuğa çıkan kimse, gittiği yerde 15 günden az kalacaksa dört rekatlı farz namazları iki rekat olarak kılar.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: TİCARET, FAİZ VE DİJİTAL MUAMELAT FETVALARI

Diyanet Fetvalarından:
- Faiz ve Riba Yasağı:
Her türlü faizli muamele İslam dininde kesin olarak haram kılınmıştır. Karşılıksız fazlalık şartı taşıyan akidler batıldır.
- Kripto Paralar ve Dijital Varlıklar:
Devlet güvencesi bulunmayan, spekülatif, haksız zenginleşmeye ve kara para aklamaya müsait belirsiz işlemler dinen caiz görülmemektedir."""
      )
    ),

    Book(
      id = "fikih_fetavayi_hindiye",
      title = "Fetâvâ-yı Hindiyye (el-Fetâva'l-Hindiyye)",
      author = "Şeyh Nizam & Hint Uleması Heyeti (Sultan Evrengzib)",
      category = CategoryType.FIKIH,
      topic = "Fetâvâ-yı Hindiyye Fıkıh ve Fetvaları",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: KİTÂBÜ'T-TAHÂRE (TEMİZLİK VE SULAR AHKÂMI)

Fetâvâ-yı Hindiyye'nin başı taharet meseleleridir:
'Taharet, şer'an necis sayılan maddeleri izale etmek ve hadesi gidermektir. Mutlak sular (yağmur, kuyu, deniz, göl suları) hem kendisi temiz hem de başkasını temizleyicidir.'""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: KİTÂBÜ'S-SALÂT (NAMAZ VE İMAMET HÜKÜMLERİ)

İmam olacak kimsede Kur'an kıraatinin sıhhati, fıkıh ilmini bilmesi ve takvası aranır. İmama uyan kimse Fatiha ve zamm-ı sureyi okumaz."""
      )
    ),

    // =========================================================================
    // 4. SİYER (Diyanet Siyer-i Nebi & M. Âsım Köksal)
    // =========================================================================
    Book(
      id = "siyer_diyanet_saricam",
      title = "Hz. Muhammed ve Evrensel Mesajı (Diyanet)",
      author = "Prof. Dr. İbrahim Sarıçam (Diyanet İşleri Başkanlığı)",
      category = CategoryType.SIYER,
      topic = "Diyanet Siyer-i Nebi Külliyatı",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: RİSALET ÖNCESİ MEKKE VE HİLFÜ'L-FUDÛL

Diyanet Siyer Külliyatı'ndan:
Miladi 6. yüzyılda Mekke, kabile asabiyetinin ve zulmün hakim olduğu bir şehirdi. Hz. Muhammed (s.a.v.), gençliğinde haksızlığa uğrayan mazlum Yemenli bir tüccarın hakkını savunmak üzere kurulan 'Hilfü'l-Fudûl' (Erdemliler Topluluğu) cemiyetine bizzat katılmıştır.
Peygamberimiz peygamberliğinden sonra dahi: 'İslam'da da böyle bir cemiyete çağrılsam tereddüt etmeden icabet ederim' buyurarak adaletin evrensel bir değer olduğunu ilan etmiştir.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: HİRA'DAN MEDİNE'YE TEVHİD MÜCADELESİ

40 yaşında Hira Nur Mağarası'nda gelen 'Yaratan Rabbinin adıyla oku!' emriyle başlayan kutlu risalet vazifesi...
Mekke'de geçirilen çetin tebliğ yılları, işkenceler, Taif taşlanması ve ardından Sevr Mağarası mucizeleriyle gerçekleşen Medine Hicreti.
Yesrib, 'Medine-i Münevvere' oldu. Resûlullah Efendimiz ilk iş olarak Mescid-i Nebevî'yi inşa ettirdi, Muhacirler ile Ensar arasında kardeşlik (Muâhât) tesis etti ve farklı inançları bir arada barış içinde yaşatan 'Medine Vesikası'nı ilan etti.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
3. KONU: VEDA HACCI VE NEBEVİ RİSALET MİRASI

Hicretin 10. yılında Arafat Meydanı'nda yüz bini aşkın ashabına hitap eden Resûlullah (s.a.v.):
'Ey İnsanlar! Canlarınız, mallarınız, ırz ve namuslarınız mukaddestir. Cahiliye adetleri ve tefeci faizleri ayaklarımın altındadır. Kadınların haklarını gözetiniz; onlar size Allah'ın emanetidir.
Ey İnsanlar! Rabbiniz birdir, babanız birdir; hepiniz Âdem'densiniz, Âdem ise topraktandır. Arabın Aceme, Acemin Araba takvadan başka hiçbir üstünlüğü yoktur!'"""
      )
    ),

    Book(
      id = "siyer_asim_koksal",
      title = "İslam Tarihi (Peygamberimiz ve Ashabı)",
      author = "M. Âsım Köksal",
      category = CategoryType.SIYER,
      topic = "M. Âsım Köksal İslam Tarihi & Siyer-i Nebi",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: CAHİLİYE KARANLIĞI VE NÜBÜVVET ŞAFAĞI

M. Âsım Köksal Merhum şaheserinde nakleder:
Rebiülevvel ayının 12. gecesi pazartesi fecrinde Hz. Âmine Hatun'un evinde bir nur doğdu: Âlemlere rahmet Muhammed Mustafa (s.a.v.). Gençliğinde sergilediği iffet ve doğruluk sebebiyle Mekkeliler O'na 'Muhammedü'l-Emîn' lakabını vermiştir.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: HİRA NUR MAĞARASI VE MEDİNE'YE HİCRET

İlk vahiy ve Yesrib'e kutlu hicret... Medine'de tarihte eşi görülmemiş kardeşlik bağı kuruldu."""
      )
    ),

    // =========================================================================
    // 5. İSLAM TARİHİ (Diyanet / TDV & İbnü'l-Esîr)
    // =========================================================================
    Book(
      id = "tarih_diyanet_islam_medeniyeti",
      title = "İslam Tarihi ve Medeniyeti (Diyanet / TDV)",
      author = "Diyanet İşleri Başkanlığı & TDV İslami Araştırmalar Heyeti",
      category = CategoryType.ISLAM_TARIHI,
      topic = "Diyanet İslam Tarihi ve Medeniyeti",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: DÖRT HALİFE DEVRİ VE ADALET ÇAĞI (HULEFÂ-İ RÂŞİDÎN)

Diyanet İslam Tarihi Seçkilerinden:
Peygamber Efendimiz'in vefatından sonra hilafet makamına geçen Hulefâ-i Râşidîn devri, İslam siyaset ahlakının zirvesidir.
Hz. Ebû Bekir devrinde ridde fitnesi bastırılmış, Kur'an Mushaf haline getirilmiştir.
Hz. Ömer devrinde adliye teşkilatı, beytülmal ve divan kurulmuş; Kudüs, Şam ve İran fethedilerek adalet sancağı dikilmiştir.
Hz. Osman devrinde Kur'an nüshaları çoğaltılmış; Hz. Ali devrinde ise ilim ve adalet mücadelesi sürdürülmüştür.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: EMEVİLER, ABBASİLER VE İLİM HAVZALARI

İslam Medeniyetinin Altın Çağı:
Bağdat'ta kurulan Beytü'l-Hikme (Hikmetler Evi) ile antik ilimler Arapçaya tercüme edilmiş; Harezmî cebiri, İbnü'l-Heysem optiği, İbn Sînâ tıbbı, Bîrûnî astronomiyi zirveye taşımıştır.
Endülüs İslam Medeniyeti (Kurtuba ve Gırnata) ise Avrupa'yı cehalet karanlığından uyandıran ilim ve mimari meşalesi olmuştur.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
3. KONU: TÜRK-İSLAM TARİHİ VE VAKIF MEDENİYETİ

Selçuklu ve Osmanlı Mirası:
Nizamiye Medreseleri ile ilim kurumsallaşmış; Selçuklu kervansarayları ticareti ve emniyeti sağlamıştır.
Osmanlı Devleti'nde ise 'İnsanı yaşat ki devlet yaşasın' felsefesiyle kurulan vakıf medeniyeti; camilerden medreselere, darüşşifalardan imarethanelere kadar toplumu ilahi şefkatle kucaklamıştır."""
      )
    ),

    Book(
      id = "tarih_ibnul_esir",
      title = "el-Kâmil fi't-Târîh (İbnü'l-Esîr Tarihi)",
      author = "İzzüddin İbnü'l-Esîr el-Cezerî",
      category = CategoryType.ISLAM_TARIHI,
      topic = "İbnü'l-Esîr Tarihi & İslam Medeniyeti",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: ÂLEMİN YARATILIŞI VE İLK PEYGAMBERLER SİLSİLESİ

Büyük İslam tarihçisi İbnü'l-Esîr mukaddimesinde şöyle buyurur:
'Tarih ilmi geçmiş milletlerin hallerini, peygamberlerin tebliğ mücadelelerini bildiren ibret dolu bir aynadır.' Hz. Âdem'den Nuh tufanına kadar tevhid mücadelesi nakledilir.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: HULEFÂ-İ RÂŞİDÎN DEVRİ VE BÜYÜK FETİHLER

Ashab-ı kiramın adaleti, Sasani ve Bizans karşısındaki fütuhatı ve İslam'ın dünyaya yayılışı tafsilatla anlatılır."""
      )
    ),

    // =========================================================================
    // 6. AKAİD (Diyanet İnanç Esasları & Nesefî)
    // =========================================================================
    Book(
      id = "akaid_diyanet_inanc_esaslari",
      title = "İslam İnanç Esasları (Akaid - Diyanet)",
      author = "Diyanet İşleri Başkanlığı (Din İşleri Yüksek Kurulu Komisyonu)",
      category = CategoryType.AKAID,
      topic = "Diyanet Temel Akaid Bilgileri",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: ALLAH'A İMAN VE ESMÂ-İ HÜSNÂ

Diyanet İslam İnanç Esasları Kitabından:
Akaid; İslam dininde kalben inanılması zaruri olan itikadi esasları inceler.
İmanın birinci ve en büyük rüknü Allah'a imandır.
Allah Teâlâ'nın Varlığı ve Sıfatları:
- Zâtî Sıfatlar (Sadece Allah'a mahsus olup mahlukatta bulunmayan sıfatlar): Vücud (Varlığı), Kıdem (Başlangıcı olmamak), Bekâ (Sonu olmamak), Vahdaniyyet (Bir olmak), Muhalefetün li'l-havâdis (Yaratılmışlara benzememek), Kıyam bi-nefsihî (Varlığı kendinden olmak).
- Sübûtî Sıfatlar: Hayat, İlim, Sem' (İşitmek), Basar (Görmek), İrade, Kudret, Kelâm ve Tekvîn (Yaratmak).""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: MELEKLERE, KİTAPLARA VE PEYGAMBERLERE İMAN

- Meleklere İman: Nurdan yaratılmış, yemeyen içmeyen, günah işlemeyen ve daima Allah'a ibadet eden ruhanî varlıklara iman.
- Kitaplara İman: Tevrat, Zebur, İncil ve tahrif edilmeden günümüze ulaşan son ilahi vahiy Kur'an-ı Kerim'e inanmak.
- Peygamberlere İman: Allah'ın kullarından seçtiği elçilere inanmaktır. Peygamberlerin sıfatları: Sıdk (doğruluk), Emanet (güvenilirlik), Tebliğ (ilahi mesajı ulaştırmak), Fetanet (üstün akıl ve feraset), İsmet (günahtan korunmuşluk).""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
3. KONU: ÂHİRET GÜNÜNE VE KAZA-KADERE İMAN

- Âhiret Günü: Kabir hayatı, kıyametin kopması, sûr borusuna üflenmesi, mizan, haşir ve sırat köprüsünden geçilerek cennet veya cehenneme varılması haktır.
- Kaza ve Kader: Hayır ve şerrin Allah'ın ilmi ve yaratmasıyla olduğuna inanmakla birlikte, kulun cüz'î iradesiyle yaptığı amellerden sorumlu tutulması Ehl-i Sünnet inancının temelidir."""
      )
    ),

    Book(
      id = "akaid_nesefiyye",
      title = "Akaidü'n-Nesefî (Metin ve Şerh)",
      author = "Necmeddin Ömer en-Nesefî",
      category = CategoryType.AKAID,
      topic = "Akaidü'n-Nesefî & Ehl-i Sünnet İnanç Esasları",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: EŞYANIN HAKİKATI VE BİLGİ SEBEPLERİ

Necmeddin en-Nesefî şöyle buyurur:
'Ehl-i hakk demiştir ki: Eşyanın hakikati sabittir ve bu hakikatleri bilmek insan için mümkündür.'
Bilgi Edinme Yolları: Selim duyular, Sadık haber ve Selim akıl.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: ALLAH TEÂLÂ'NIN ZÂTI VE SIFATLARI

Âlem sonradan yaratılmıştır (hâdistir). Onu yoktan var eden Kadîm Yaratıcı Allah'tır."""
      )
    ),

    // =========================================================================
    // 7. AHLAK (Diyanet İslam Ahlakı & İmam Gazâlî)
    // =========================================================================
    Book(
      id = "ahlak_diyanet_islam_ahlaki",
      title = "İslam Ahlakı ve Yaşayan Değerlerimiz (Diyanet)",
      author = "Prof. Dr. Mustafa Çağrıcı (Diyanet İşleri Başkanlığı)",
      category = CategoryType.AHLAK,
      topic = "Diyanet İslam Ahlakı ve Yaşayan Değerler",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: İSLAM AHLAKININ TEMELLERİ VE İHLAS

Diyanet İslam Ahlakı Kitabından:
Ahlak, insanın yaratılışındaki güzelliği davranışlarına yansıtmasıdır. Kur'an-ı Kerim Resûlullah'ı (s.a.v.) överek: 'Şüphesiz sen pek yüce bir ahlak üzeresin' (Kalem Sûresi 4) buyurmuştur.
İhlas ve Samimiyet:
İslam ahlakında riyadan, gösterişten ve menfaatten uzak durmak esastır. Amelin kıymeti, kalpte taşınan niyetin safiyetine bağlıdır. Müminin sözü ile özü bir olmalıdır.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: ADALET, DOĞRULUK VE KUL HAKKI

- Adalet: Her hakkı sahibine vermek, sevdiğine de sevmediğine de adaletle muamele etmektir. 'Bir kavme olan kininiz, sizi adaletsizlik yapmaya sevk etmesin' (Mâide Sûresi 8).
- Sıdk (Doğruluk): Müslümanın şiarıdır. Peygamberimiz: 'Doğruluk iyiliğe, iyilik cennete götürür' buyurmuştur.
- Kul Hakkı: İslam'da affı en zor olan günahtır; hak sahibi hakkını helal etmedikçe Allah kul hakkını affetmez.""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
3. KONU: MERHAMET, AFFEDİCİLİK VE GÖNÜL DÜNYASI

- Merhamet ve Şefkat: Yaratılanı Yaratan'dan ötürü sevmek İslam irfanının özüdür.
- Sabır ve Şükür: Varlıkta şükür, darlıkta sabır müminin iki kanadıdır.
- Kibir ve Hasetten Arınma: Tevazu insanı yüceltir; kibir ise İblis'in dergah-ı izzetten kovulma sebebidir. Mümin kalbini kin, haset ve kibirden temizleyerek huzura erer."""
      )
    ),

    Book(
      id = "ahlak_ihyau_ulumiddin",
      title = "İhyâu Ulûmi'd-Dîn (Din İlimlerinin İhyası)",
      author = "Hüccetü'l-İslam İmam Gazâlî",
      category = CategoryType.AHLAK,
      topic = "İhyâu Ulûmi'd-Dîn Ahlak ve Kalp İlimleri",
      pages = listOf(
        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
1. KONU: KİTÂBÜ'L-İLM (İLMİN VE ÂLİMLERİN FAZİLETİ)

Hüccetü'l-İslam İmam Gazâlî buyurur:
'İlim tahsili, amellerin en faziletlisidir. Resûlullah (s.a.v.) buyurdu: İlim talebi her Müslümana farzdır. Âlimin âbide (ibadet edene) üstünlüğü, dolunayın diğer yıldızlara üstünlüğü gibidir.'""",

        """BİSMİLLÂHİRRAHMÂNİRRAHÎM
2. KONU: KİTÂBÜ ACÂİBİ'L-KALB (KALBİN ACÂİPLİKLERİ VE HALLERİ)

Bil ki kalp, insan bedenindeki mülkün sultanıdır. Azalar ise o sultana itaat eden ordulardır. Sultan salih ve pak olursa ordu da adalet üzere olur.
Peygamberimiz ferman buyurdu: 'Bedende bir et parçası vardır; o salih olursa bütün beden salih olur. O kalptir!' (Buhârî, Îmân 39)"""
      )
    )
  )
}
