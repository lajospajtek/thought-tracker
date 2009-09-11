using System.Collections.Generic;

namespace ThoughtTracker.Blogspot.Com.Enumerating {
    internal class EnumeratingOnPairs<T1, T2> : IEnumeratingOnPairs<T1, T2> {
        private readonly IEnumerable<T1> _source1;
        private readonly IEnumerable<T2> _source2;

        public EnumeratingOnPairs(IEnumerable<T1> source1, IEnumerable<T2> source2) {
            _source1 = source1;
            _source2 = source2;
        }

        public ICollection<Tuple<T1, T2>> Zip() {
            List<Tuple<T1, T2>> result = new List<Tuple<T1, T2>>();

            ForEachPair(delegate(T1 t1, T2 t2) { result.Add(new Tuple<T1, T2>(t1, t2)); });

            return result;
        }


        public void ForEachPair(Action<T1, T2> action) {
            IEnumerator<T1> it1 = _source1.GetEnumerator();
            IEnumerator<T2> it2 = _source2.GetEnumerator();

            while (it1.MoveNext() && it2.MoveNext()) {
                action(it1.Current, it2.Current);
            }
        }
    }
}