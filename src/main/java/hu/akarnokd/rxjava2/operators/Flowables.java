/*
 * Copyright 2016-2017 David Karnok
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.akarnokd.rxjava2.operators;

import java.util.Comparator;
import java.util.concurrent.*;

import org.reactivestreams.Publisher;

import io.reactivex.*;
import io.reactivex.annotations.*;
import io.reactivex.internal.functions.*;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * Utility class to create Flowable sources.
 */
public final class Flowables {
    /** Utility class. */
    private Flowables() {
        throw new IllegalStateException("No instances!");
    }


    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by their natural order).
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T extends Comparable<? super T>> Flowable<T> orderedMerge(Publisher<T>... sources) {
        return orderedMerge(Functions.naturalOrder(), false, Flowable.bufferSize(), sources);
    }


    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by their natural order) and allows delaying any error they may signal.
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @param delayErrors if true, source errors are delayed until all sources terminate in some way
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T extends Comparable<? super T>> Flowable<T> orderedMerge(boolean delayErrors, Publisher<T>... sources) {
        return orderedMerge(Functions.naturalOrder(), delayErrors, Flowable.bufferSize(), sources);
    }


    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by their natural order), allows delaying any error they may signal and sets the prefetch
     * amount when requesting from these Publishers.
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @param delayErrors if true, source errors are delayed until all sources terminate in some way
     * @param prefetch the number of items to prefetch from the sources
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T extends Comparable<? super T>> Flowable<T> orderedMerge(boolean delayErrors, int prefetch, Publisher<T>... sources) {
        return orderedMerge(Functions.naturalOrder(), delayErrors, prefetch, sources);
    }


    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by the Comparator).
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @param comparator the comparator to use for comparing items;
     *                   it is called with the last known smallest in its first argument
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Flowable<T> orderedMerge(Comparator<? super T> comparator, Publisher<T>... sources) {
        return orderedMerge(comparator, false, Flowable.bufferSize(), sources);
    }


    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by the Comparator) and allows delaying any error they may signal.
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @param comparator the comparator to use for comparing items;
     *                   it is called with the last known smallest in its first argument
     * @param delayErrors if true, source errors are delayed until all sources terminate in some way
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Flowable<T> orderedMerge(Comparator<? super T> comparator, boolean delayErrors, Publisher<T>... sources) {
        return orderedMerge(comparator, delayErrors, Flowable.bufferSize(), sources);
    }


    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by the Comparator), allows delaying any error they may signal and sets the prefetch
     * amount when requesting from these Publishers.
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @param comparator the comparator to use for comparing items;
     *                   it is called with the last known smallest in its first argument
     * @param delayErrors if true, source errors are delayed until all sources terminate in some way
     * @param prefetch the number of items to prefetch from the sources
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Flowable<T> orderedMerge(Comparator<? super T> comparator, boolean delayErrors, int prefetch, Publisher<T>... sources) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly(new FlowableOrderedMerge<T>(sources, null, comparator, delayErrors, prefetch));
    }

    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by the Comparator).
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @param comparator the comparator to use for comparing items;
     *                   it is called with the last known smallest in its first argument
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Flowable<T> orderedMerge(Iterable<? extends Publisher<T>> sources, Comparator<? super T> comparator) {
        return orderedMerge(sources, comparator, false, Flowable.bufferSize());
    }


    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by the Comparator) and allows delaying any error they may signal.
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @param comparator the comparator to use for comparing items;
     *                   it is called with the last known smallest in its first argument
     * @param delayErrors if true, source errors are delayed until all sources terminate in some way
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Flowable<T> orderedMerge(Iterable<? extends Publisher<T>> sources, Comparator<? super T> comparator, boolean delayErrors) {
        return orderedMerge(sources, comparator, delayErrors, Flowable.bufferSize());
    }

    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by the Comparator), allows delaying any error they may signal and sets the prefetch
     * amount when requesting from these Publishers.
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @param comparator the comparator to use for comparing items;
     *                   it is called with the last known smallest in its first argument
     * @param delayErrors if true, source errors are delayed until all sources terminate in some way
     * @param prefetch the number of items to prefetch from the sources
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Flowable<T> orderedMerge(Iterable<? extends Publisher<T>> sources, Comparator<? super T> comparator, boolean delayErrors, int prefetch) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly(new FlowableOrderedMerge<T>(null, sources, comparator, delayErrors, prefetch));
    }


    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by their natural order).
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T extends Comparable<? super T>> Flowable<T> orderedMerge(Iterable<? extends Publisher<T>> sources) {
        return orderedMerge(sources, Functions.naturalOrder(), false, Flowable.bufferSize());
    }


    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by their natural order) and allows delaying any error they may signal.
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @param delayErrors if true, source errors are delayed until all sources terminate in some way
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T extends Comparable<? super T>> Flowable<T> orderedMerge(Iterable<? extends Publisher<T>> sources, boolean delayErrors) {
        return orderedMerge(sources, Functions.naturalOrder(), delayErrors, Flowable.bufferSize());
    }


    /**
     * Merges the source Publishers in an ordered fashion picking the smallest of the available value from
     * them (determined by their natural order), allows delaying any error they may signal and sets the prefetch
     * amount when requesting from these Publishers.
     * @param <T> the value type of all sources
     * @param sources the iterable sequence of sources
     * @param delayErrors if true, source errors are delayed until all sources terminate in some way
     * @param prefetch the number of items to prefetch from the sources
     * @return the new Flowable instance
     * 
     * @since 0.8.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T extends Comparable<? super T>> Flowable<T> orderedMerge(Iterable<? extends Publisher<T>> sources, boolean delayErrors, int prefetch) {
        return orderedMerge(sources, Functions.naturalOrder(), delayErrors, prefetch);
    }

    /**
     * Repeats a scalar value indefinitely.
     * @param <T> the value type
     * @param item the value to repeat
     * @return the new Flowable instance
     * 
     * @since 0.14.2
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Flowable<T> repeat(T item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return RxJavaPlugins.onAssembly(new FlowableRepeatScalar<T>(item));
    }

    /**
     * Repeatedly calls the given Callable to produce items indefinitely.
     * @param <T> the value type
     * @param callable the Callable to call
     * @return the new Flowable instance
     * 
     * @since 0.14.2
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Flowable<T> repeatCallable(Callable<T> callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly(new FlowableRepeatCallable<T>(callable));
    }

    /**
     * Periodically tries to emit an ever increasing long value or
     * buffers (efficiently) such emissions until the downstream requests.
     * <dl>
     *  <dt><b>Backpressure:</b></dt>
     *  <dd>The operator honors the backpressure of the downstream and
     *  no emission is lost, however, the timing of the reception of the
     *  values is now dependent on the downstream backpressure.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>The operator uses the {@code computation} {@link Scheduler} to time
     *  the emission and likely deliver the value (unless backpressured).</dd>
     * </dl>
     * 
     * @param period the emission period (including the delay for the first emission)
     * @param unit the emission time unit
     * @return the new Flowable instance
     * 
     * @since 0.15.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public static Flowable<Long> intervalBackpressure(long period, TimeUnit unit) {
        return intervalBackpressure(period, period, unit, Schedulers.computation());
    }

    /**
     * Periodically tries to emit an ever increasing long value or
     * buffers (efficiently) such emissions until the downstream requests.
     * <dl>
     *  <dt><b>Backpressure:</b></dt>
     *  <dd>The operator honors the backpressure of the downstream and
     *  no emission is lost, however, the timing of the reception of the
     *  values is now dependent on the downstream backpressure.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>The operator uses the {@code computation} {@link Scheduler} to time
     *  the emission and likely deliver the value (unless backpressured).</dd>
     * </dl>
     * 
     * @param period the emission period (including the delay for the first emission)
     * @param unit the emission time unit
     * @param scheduler the scheduler to use for timing and likely emitting items
     * @return the new Flowable instance
     * 
     * @since 0.15.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public static Flowable<Long> intervalBackpressure(long period, TimeUnit unit, Scheduler scheduler) {
        return intervalBackpressure(period, period, unit, scheduler);
    }

    /**
     * Periodically tries to emit an ever increasing long value or
     * buffers (efficiently) such emissions until the downstream requests.
     * <dl>
     *  <dt><b>Backpressure:</b></dt>
     *  <dd>The operator honors the backpressure of the downstream and
     *  no emission is lost, however, the timing of the reception of the
     *  values is now dependent on the downstream backpressure.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>The operator uses the {@code computation} {@link Scheduler} to time
     *  the emission and likely deliver the value (unless backpressured).</dd>
     * </dl>
     * 
     * @param initialDelay the initial delay before emitting the first 0L
     * @param period the emission period after the first emission
     * @param unit the emission time unit
     * @return the new Flowable instance
     * 
     * @since 0.15.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public static Flowable<Long> intervalBackpressure(long initialDelay, long period, TimeUnit unit) {
        return intervalBackpressure(initialDelay, period, unit, Schedulers.computation());
    }

    /**
     * Periodically tries to emit an ever increasing long value or
     * buffers (efficiently) such emissions until the downstream requests.
     * <dl>
     *  <dt><b>Backpressure:</b></dt>
     *  <dd>The operator honors the backpressure of the downstream and
     *  no emission is lost, however, the timing of the reception of the
     *  values is now dependent on the downstream backpressure.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>The operator uses the{@link Scheduler} provided to time
     *  the emission and likely deliver the value (unless backpressured).</dd>
     * </dl>
     * 
     * @param initialDelay the initial delay before emitting the first 0L
     * @param period the emission period (including the delay for the first emission)
     * @param unit the emission time unit
     * @param scheduler the scheduler to use for timing and likely emitting items
     * @return the new Flowable instance
     * 
     * @since 0.15.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public static Flowable<Long> intervalBackpressure(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        return RxJavaPlugins.onAssembly(new FlowableIntervalBackpressure(initialDelay, period, unit, scheduler));
    }
}
