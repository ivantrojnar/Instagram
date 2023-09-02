package hr.itrojnar.instagram.designpatterns.strategy

class CloudSave : SaveStrategy {
    override fun save(data: Map<String, Any?>) {
        println("Data saved to the cloud")
    }
}