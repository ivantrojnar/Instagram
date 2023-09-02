package hr.itrojnar.instagram.designpatterns.strategy

class SettingsSaver(private var strategy: SaveStrategy) {

    fun setStrategy(newStrategy: SaveStrategy) {
        strategy = newStrategy
    }

    fun saveSettings(data: Map<String, Any?>) {
        strategy.save(data)
    }
}