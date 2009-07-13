using System.Collections.Generic;

namespace ThoughtTracker.Blogspot.Com.Enumerating {
    public interface IEnumeratingOnPairs<T1, T2> {
        ICollection<Tuple<T1, T2>> Zip();
        void ForEachPair(Action<T1, T2> action);
    }
}