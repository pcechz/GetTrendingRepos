package com.pcechz.getmega.data.mapper

import com.google.gson.annotations.SerializedName
import com.pcechz.getmega.data.api.RepoResponse
import com.pcechz.getmega.data.model.ItemHolder
import com.pcechz.getmega.data.model.Owner
import com.pcechz.getmega.data.model.ItemHolder.Repo
import java.util.*

class RepoMapper {
    fun transform(response: RepoResponse): ItemHolder {
        return with(response) {

            ItemHolder(
                itemCount = total,
                results = items.map {
                    Repo(
                        it.id,
                        it.name,
                        it.fullName,
                        it.description,
                        it.url,
                        it.stars,
                        it.forks,
                        it.language,
                        it.languageColor,
                        it.watchers,
                        it.owner,
                        it.createDate,
                        it.updateDate,
                        it.openIssues,
                        it.expand
                    )
                }
            )
        }
    }
}