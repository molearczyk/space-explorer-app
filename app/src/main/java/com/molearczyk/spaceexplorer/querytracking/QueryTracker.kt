package com.molearczyk.spaceexplorer.querytracking

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueryTracker @Inject constructor() {

    private val latestQuery: BehaviorProcessor<TextQuery> = BehaviorProcessor.createDefault(NoQuery)

    fun onTrackQueryInvoked(queryFlow: Single<TextQuery>): Single<TextQuery> {
        return queryFlow.doOnSuccess(latestQuery::onNext)
    }

    fun checkLatestQuery(): Maybe<TextQuery> {
        return latestQuery.onBackpressureLatest().firstOrError().filter { it != NoQuery }
    }

    companion object {
        val NoQuery = TextQuery(null)
    }
}