package hr.itrojnar.instagram.enums

import hr.itrojnar.instagram.R

enum class Subscription(
    val id: Int,
    val titleResId: Int,
    val descriptionResId: Int,
    val uploadLimit: Int,  // in MB
    val photoLimit: Int    // number of photos
) {
    FREE(1, R.string.free, R.string.upload_limit_1gb_day_max_spend_10_photos, 10, 10),
    PRO(2, R.string.pro, R.string.upload_limit_10gb_day_max_spend_50_photos_price_9_99_month, 100, 50),
    GOLD(3, R.string.gold, R.string.upload_limit_100gb_day_max_spend_100_photos_price_24_99_month, 1024, 100)
}