package com.eddy.nrf.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import com.eddy.nrf.utils.Uuid
import java.util.UUID

class BluetoothServiceBuilder {
    private val service: BluetoothGattService

    constructor(uuid: UUID, serviceType: Int) {
        service = BluetoothGattService(uuid, serviceType)
    }

    fun addCharacteristic(
        uuid: UUID,
        properties: Int,
        permissions: Int
    ): CharacteristicBuilder {
        val characteristic = BluetoothGattCharacteristic(uuid, properties, permissions)
        service.addCharacteristic(characteristic)
        return CharacteristicBuilder(characteristic)
    }

    fun build(): BluetoothGattService = service

    inner class CharacteristicBuilder(private val characteristic: BluetoothGattCharacteristic) {
        fun addDescriptor(
            uuid: UUID,
            permissions: Int
        ): CharacteristicBuilder {
            val descriptor = BluetoothGattDescriptor(uuid, permissions)
            characteristic.addDescriptor(descriptor)
            return this
        }

        fun and(): BluetoothServiceBuilder = this@BluetoothServiceBuilder
    }

    companion object {
        fun createHeartRateService(): BluetoothGattService {
            return BluetoothServiceBuilder(
                Uuid.HEART_RATE_SERVICE,
                BluetoothGattService.SERVICE_TYPE_PRIMARY
            )
                .addCharacteristic(
                    Uuid.HEART_RATE_MEASUREMENT,
                    BluetoothGattCharacteristic.PROPERTY_READ or
                            BluetoothGattCharacteristic.PROPERTY_NOTIFY or
                            BluetoothGattCharacteristic.PROPERTY_WRITE,
                    BluetoothGattCharacteristic.PERMISSION_READ or
                            BluetoothGattCharacteristic.PERMISSION_WRITE
                )
                .addDescriptor(
                    Uuid.CLIENT_CONFIG,
                    BluetoothGattDescriptor.PERMISSION_READ or
                            BluetoothGattDescriptor.PERMISSION_WRITE
                )
                .and()
                .build()
        }
    }
}