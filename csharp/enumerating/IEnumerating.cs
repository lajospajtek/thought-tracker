using System;
using System.Collections.Generic;
using ThoughtTracker.Blogspot.Com.Enumerating;
namespace ThoughtTracker.Blogspot.Com.Enumerating {
    public interface IEnumerating<T> {
        void ForEachWithIndex(Action<T, int> action);
        void ForEach(Action<T> action);
        IEnumerating<R> Collect<R>(Converter<T, R> convert);
        IEnumerating<R> Map<R>(Converter<T, R> convert);
        IEnumerating<T> Select(Predicate<T> satisfies);
        IEnumerating<T> FindAll(Predicate<T> satisfies);
        IEnumerating<T> Filter(Predicate<T> satisfies);
        IEnumerating<T> Reject(Predicate<T> satisfies);
        IEnumerating<R> SelectThenCollect<R>(Predicate<T> satisfies, Converter<T, R> converter);
        void SelectThenApply(Predicate<T> satisfies, Action<T> action);
        bool HasAnySatisfying(Predicate<T> satisfies);
        bool Exists(Predicate<T> satisfies);
        bool AreAllSatisfying(Predicate<T> satisfies);
        bool TrueForAll(Predicate<T> satisfies);
        IEnumerating<T> Sort(Comparison<T> comparison);
        R Inject<R>(R initial_value, Func<R, R, T> action);
        T Find(Predicate<T> satisfies);
        void FindThenApply(Predicate<T> satisfies, Action<T> action);
        T GetFirst();
        ICollection<T> ToCollection();
    }
}