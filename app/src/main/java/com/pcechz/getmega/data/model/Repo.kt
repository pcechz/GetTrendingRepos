package com.pcechz.getmega.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.pcechz.getmega.utils.JsonReaderObservable
import io.reactivex.Observable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Parcelize
data class ItemHolder(
    @JsonAdapter(ObservableTypeAdapterFactory::class)

    @SerializedName("total_count")
    val itemCount: Int,

    val page: Int = 0,

    @SerializedName("items")
    val results: List<Repo>,

    val hasMore: Boolean = itemCount > page

): Parcelable {

    @IgnoredOnParcel
    val endOfPage = itemCount == page


    @Parcelize
    @Entity(tableName = "repos")

    data class Repo(
        @PrimaryKey(autoGenerate = true)
        @SerializedName("id")
        val id: Long = 0,
        @SerializedName("name")
        val name: String,

        @SerializedName("full_name")
        val fullName: String,

        @SerializedName("description")
        val description: String?,

        @SerializedName("html_url")
        val url: String,

        @SerializedName("stargazers_count")
        val stars: Int,

        @SerializedName("forks_count")
        val forks: Int,

        @SerializedName("language")
        val language: String?,

        @SerializedName("languageColor")
        val languageColor: String?,

        @SerializedName("watchers")
        val watchers: Int,
        @Expose
        @Embedded
        @SerializedName("owner")
        val owner: Owner,

        @SerializedName("created_at")
        val createDate: String,

        @SerializedName("updated_at")
        val updateDate: String,

        @SerializedName("open_issues")
        val openIssues: Int,

        var expand: Boolean = false

    ) : Parcelable

    @Parcelize
    @Entity(tableName = "repo_remote_keys")
    data class RepoRemoteKeys(
        @PrimaryKey val repoId: Long,
        val prevKey: Int?,
        val nextKey: Int?
    ) : Parcelable
}

class ObservableTypeAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        return if (Observable::class.java.isAssignableFrom(type.rawType)) {
            ObservableTypeAdapter<T>(type.type.getObservableParameterType(), gson) as TypeAdapter<T>
        } else null
    }

}
class ObservableTypeAdapter<T>(private val elementType: Type, private val gson: Gson) : TypeAdapter<Observable<T>>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, observable: Observable<T>) {
        out.beginArray()
        observable.blockingForEach { gson.toJson(it, elementType, out) }
        out.endArray()
    }

    override fun read(reader: JsonReader): Observable<T> {
        return JsonReaderObservable(elementType, gson, reader)
    }

}

private fun Type.getObservableParameterType(): Type = getTParameterType(Observable::class.java)

private fun Type.getTParameterType(expectedParameterizedType: Type): Type {
    if (expectedParameterizedType == this) {
        return expectedParameterizedType
    }
    if (this is ParameterizedType) {
        if (expectedParameterizedType == rawType) {
            val actualTypeArguments = actualTypeArguments
            if (actualTypeArguments.size == 1) {
                return actualTypeArguments[0]
            }
        }
    }
    throw IllegalArgumentException(toString())
}