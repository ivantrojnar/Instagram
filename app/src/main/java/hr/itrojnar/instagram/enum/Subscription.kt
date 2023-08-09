package hr.itrojnar.instagram.enum

enum class Subscription(
    val id: Int,
    val title: String,
    val description: String,
    val uploadLimit: Int,  // in MB
    val photoLimit: Int    // number of photos
) {
    FREE(1, "FREE", "Upload limit: 1GB/day. Max spend: 10 photos.", 1024, 10),
    PRO(2, "PRO", "Upload limit: 10GB/day. Max spend: 50 photos. Price: $9.99/month", 10240, 50),
    GOLD(3, "GOLD", "Upload limit: 100GB/day. Max spend: 100 photos. Price: $24.99/month", 102400, 100)
}