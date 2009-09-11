using System;
using System.Collections.Generic;

namespace ThoughtTracker.Blogspot.Com.Enumerating {
    /// <summary>
    /// Support for generic collections.
    /// </summary>
    internal class EnumeratingOnOneSource<S> : IEnumerating<S> {
        private readonly IEnumerable<S> _source;


        internal EnumeratingOnOneSource(IEnumerable<S> source) {
            _source = source;
        }


        public void ForEachWithIndex(Action<S, int> action) {
            int index = 0;
            foreach (S item in _source) {
                action(item, index);
                index += 1;
            }
        }


        public void ForEach(Action<S> action) {
            foreach (S item in _source) action(item);
        }


        public IEnumerating<R> Collect<R>(Converter<S, R> convert) {
            return Map(convert);
        }


        public IEnumerating<R> Map<R>(Converter<S, R> convert) {
            List<R> collected = new List<R>();
            foreach (S item in _source) collected.Add(convert(item));
            return new EnumeratingOnOneSource<R>(collected);
        }


        public IEnumerating<S> Select(Predicate<S> satisfies) {
            return Filter(satisfies);
        }


        public IEnumerating<S> FindAll(Predicate<S> satisfies) {
            return Filter(satisfies);
        }


        public IEnumerating<S> Filter(Predicate<S> satisfies) {
            List<S> collected = new List<S>();
            foreach (S item in _source) if (satisfies(item)) collected.Add(item);

            return new EnumeratingOnOneSource<S>(collected);
        }


        public IEnumerating<S> Reject(Predicate<S> satisfies) {
            List<S> collected = new List<S>();
            foreach (S item in _source) if (!satisfies(item)) collected.Add(item);

            return new EnumeratingOnOneSource<S>(collected);
        }


        public IEnumerating<R> SelectThenCollect<R>(Predicate<S> satisfies, Converter<S, R> converter) {
            List<R> collected = new List<R>();
            foreach (S item in _source) if (satisfies(item)) collected.Add(converter(item));
            return new EnumeratingOnOneSource<R>(collected);
        }


        public void SelectThenApply(Predicate<S> satisfies, Action<S> action) {
            foreach (S item in _source) if (satisfies(item)) action(item);
        }


        public bool HasAnySatisfying(Predicate<S> satisfies) {
            foreach (S candidate in _source) if (satisfies.Invoke(candidate)) return true;
            return false;
        }


        public bool Exists(Predicate<S> satisfies) {
            return HasAnySatisfying(satisfies);
        }


        public bool AreAllSatisfying(Predicate<S> satisfies) {
            foreach (S candidate in _source) if (!satisfies.Invoke(candidate)) return false;
            return true;
        }


        public bool TrueForAll(Predicate<S> satisfies) {
            return AreAllSatisfying(satisfies);
        }


        public IEnumerating<S> Sort(Comparison<S> comparison) {
            List<S> result = new List<S>(_source);
            result.Sort(comparison);
            return new EnumeratingOnOneSource<S>(result);
        }


        public R Inject<R>(R initial_value, Func<R, R, S> action) {
            R accumulator = initial_value;
            foreach (S item in _source) accumulator = action(accumulator, item);
            return accumulator;
        }


        public S Find(Predicate<S> satisfies) {
            foreach (S candidate in _source) if (satisfies.Invoke(candidate)) return candidate;

            throw new ArgumentException("Object not found");
        }


        public void FindThenApply(Predicate<S> satisfies, Action<S> action) {
            foreach (S item in _source)
                if (satisfies(item)) {
                    action(item);
                    return;
                }
        }


        public S GetFirst() {
            foreach (S candidate in _source) return candidate;

            throw new ArgumentException(string.Format("No elements were found"));
        }


        public ICollection<S> ToCollection() {
            return new List<S>(_source);
        }
    }
}