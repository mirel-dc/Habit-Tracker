package com.example.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.UUID

val gson: Gson = GsonBuilder()
    .setPrettyPrinting()
    //.registerTypeAdapter(UUID::class.java, UUIDTypeAdapter())
    // .registerTypeAdapter(HabitTypeTypeAdapter::class.java, HabitTypeTypeAdapter())
    .create()

//TODO
class UUIDTypeAdapter : TypeAdapter<UUID>() {
    override fun write(out: JsonWriter?, value: UUID?) {
        out?.value(value?.toString())
    }

    override fun read(`in`: JsonReader?): UUID {
        val uuidString = `in`?.nextString()
        return UUID.fromString(uuidString)
    }
}

//class HabitTypeTypeAdapter : TypeAdapter<HabitType>() {
//    override fun write(out: JsonWriter, value: HabitType?) {
//        out.beginObject()
//        if (value != null) {
//            out.name("type").value(value.value)
//        }
//        out.endObject()
//    }
//
//    override fun read(`in`: JsonReader): HabitType {
//        `in`.beginObject()
//        var value = 0
//        while (`in`.hasNext()) {
//            when (`in`.nextName()) {
//                "type" -> value = `in`.nextInt()
//                else -> `in`.skipValue()
//            }
//        }
//        `in`.endObject()
//        return HabitType.getHabitTypeByValue(value)
//    }
//}
