package hr.itrojnar.instagram.designpatterns.strategy

class LocalSave : SaveStrategy {
    override fun save(data: Map<String, Any?>) {
        println("Data saved locally")
    }
}