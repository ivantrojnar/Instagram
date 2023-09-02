package hr.itrojnar.instagram.designpatterns.strategy

interface SaveStrategy {
    fun save(data: Map<String, Any?>)
}