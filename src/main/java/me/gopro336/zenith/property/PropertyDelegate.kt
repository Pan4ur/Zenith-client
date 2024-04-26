package me.gopro336.zenith.property

import kotlin.reflect.KProperty

/**
 * @author Gopro336
 */
class PropertyDelegate<F: Any, out T: Property<F>>(val value: T) {
	fun getActualValue(): T = value
	
	operator fun getValue(thisRef: Any?, property: KProperty<*>): F {
		return value.value
	}
	
	operator fun setValue(thisRef: Any?, property: KProperty<*>, newVal: F) {
		value.value = newVal
	}
}
