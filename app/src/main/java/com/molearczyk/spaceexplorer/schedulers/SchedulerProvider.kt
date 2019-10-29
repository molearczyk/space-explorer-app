package com.molearczyk.spaceexplorer.schedulers

import io.reactivex.Scheduler

interface SchedulerProvider {

    fun io(): Scheduler

    fun mainThread(): Scheduler
}