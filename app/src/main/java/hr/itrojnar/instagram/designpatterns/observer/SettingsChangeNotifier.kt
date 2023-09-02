package hr.itrojnar.instagram.designpatterns.observer

class SettingsChangeNotifier {
    private val observers: MutableList<SettingsObserver> = mutableListOf()

    fun addObserver(observer: SettingsObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: SettingsObserver) {
        observers.remove(observer)
    }

    fun notifyChange() {
        observers.forEach { it.onSettingChanged() }
    }
}