package com.molearczyk.spaceexplorer.querytracking

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueryTracker @Inject constructor() {


    private val latestQuery: BehaviorProcessor<Query> = BehaviorProcessor.createDefault(Query(null))


    fun onTrackQueryInvoked(queryFlow: Single<Query>): Single<Query> {
        return queryFlow.doOnSuccess(latestQuery::onNext)
    }


    fun observeQueries(): Flowable<Query> {
        return latestQuery.onBackpressureLatest().filter { it.keywords != null }
    }

    fun checkLatestQuery(): Maybe<Query> {
        return latestQuery.onBackpressureLatest().firstOrError().filter { it.keywords != null }
    }
}