// Copyright (c) 2005 - 2009, Clark & Parsia, LLC. <http://www.clarkparsia.com>

package com.clarkparsia.utils.collections;

import com.clarkparsia.utils.Predicate;
import com.clarkparsia.utils.Function;
import com.clarkparsia.utils.DataCommand;
import com.clarkparsia.utils.BasicUtils;

import java.util.Set;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;
import java.util.AbstractCollection;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;

/**
 * Title: CollectionUtil<br/>
 * Description: Utility methods for using and working with collections.<br/>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com> <br/>
 * Created: Apr 21, 2009 10:15:38 AM <br/>
 *
 * @author Michael Grove <mike@clarkparsia.com>
 */
public class CollectionUtil {

	/**
	 * Returns the number of elements in the iterator
	 * @param theIter the iterator to count
	 * @return the number of elements
	 */
	public static int size(Iterator<?> theIter) {
		int aCount = 0;

		while (theIter.hasNext()) {
			aCount++;
		}

		return aCount;
	}

	public static Map<String, String> map(Properties theProps) {
		Map<String, String> aMap = new HashMap<String, String>();

		for (Object aKey : theProps.keySet()) {
			aMap.put(aKey.toString(), theProps.getProperty(aKey.toString()));
		}

		return aMap;
	}

	public static <T> void remove(Collection<T> theCollection, Predicate<? super T> thePred) {
		remove(theCollection.iterator(), thePred);
	}

	public static <T> void remove(Iterator<T> theIterator, Predicate<? super T> thePred) {
		while (theIterator.hasNext()) {
			T aObj = theIterator.next();
			if (!thePred.accept(aObj)) {
				theIterator.remove();
			}
		}
	}

	public static <T> Set<T> set(Iterable<T> theIter) {
		return set(theIter.iterator());
	}

    public static <T> Set<T> set(Iterator<T> theIter) {
        Set<T> aSet = new LinkedHashSet<T>();

        while (theIter.hasNext())
            aSet.add(theIter.next());

        return aSet;
    }

	public static <T> List<T> list(Iterable<T> theIter) {
		return list(theIter.iterator());
	}

    public static <T> List<T> list(Iterator<T> theIter) {
        List<T> aList = new ArrayList<T>();

        while (theIter.hasNext()) {
            aList.add(theIter.next());
		}

        return aList;
    }

	public static <T> Iterable<T> iterable(final Iterator<T> theIter) {
		return new Iterable<T>() {
			public Iterator<T> iterator() {
				return theIter;
			}
		};
	}

    /**
     * Checks to see if any elements of one set are present in another.
     * First parameter is the list of elements to search for, the second
     * parameter is the list to search in.  Basically tests to see if the two sets intersect
     * @param theList Set the elements to look for
     * @param toSearch Set the search set
     * @return boolean true if any element in the first set is present in the second
     */
    public static <T> boolean containsAny(Collection<T> theList, Collection<T> toSearch) {
        if (toSearch.isEmpty()) {
            return false;
        }
        else {
            for (T aObj : theList) {
                if (toSearch.contains(aObj)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static <T> boolean containsAll(Collection<T> theList, Collection<T> toSearch) {
        if (toSearch.isEmpty()) {
            return true;
        }
        else {
            for (T aObj : toSearch) {
                if (!theList.contains(aObj)) {
                    return false;
                }
            }
            return true;
        }
    }

	/**
     * Convienence method for creating a set that is the union of N other sets
     * @param theSets the sets to create a union of
     * @param <T> the type of the sets
     * @return a set which is the union of all the passed in sets
     */
    public static <T> Set<T> union(Set<T>... theSets) {
        Set<T> aUnion = new HashSet<T>();

        for (Set<T> aSet : theSets) {
            aUnion.addAll(aSet);
        }

        return aUnion;
    }

    /**
     * Convienence method for creating a set that is the intersection of all N sets
     * @param theSets the sets to create the intersection from
     * @param <T> the type of the sets
     * @return a new set which is the intersection of the provided sets
     */
    public static <T> Set<T> intersection(Set<T>... theSets) {
        Set<T> aIntersection = new HashSet<T>();

        boolean aFirst = true;
        for (Set<T> aSet : theSets) {
            if (aFirst) {
                aFirst = false;
                aIntersection.addAll(aSet);
            }
            else {
                aIntersection.retainAll(aSet);
            }
        }

        return aIntersection;
    }

    /**
     * Returns true if two collections have the same contents.  This is used to compare two collections which may or may
     * not be the same type, such as comparing a list and a set, which normally will not be equals even if they
     * contain the same set of values.  This does an equals comparision on the contents of the collections.
     * @param theList the first collection to compare
     * @param theCompare the second collection to compare
     * @param <T> the object type in the collections
     * @return true if they have the same contents, false otherwise
     */
    public static <T> boolean contentsEqual(Collection<T> theList, Collection<T> theCompare) {
        if (theList.size() != theCompare.size()) {
            return false;
        }

        for (T aListElem : theList) {
            Iterator aCompareIter = theCompare.iterator();

            boolean found = false;
            while (aCompareIter.hasNext()) {
                Object aCompareItem = aCompareIter.next();

                if (aListElem.equals(aCompareItem)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

	public static <T> Collection<T> filter(Collection<T> theCollection, Predicate<T> theFilter) {
		return filter(theCollection.iterator(), theFilter);
	}

	/**
	 * Filter the contents of an iterator based on the filter provided
	 * @param theIterator the collection to filter
	 * @param theFilter the filter to use
	 * @param <T> the type of objects in the iterator
	 * @return a copy of the elements in the iterator, but filtered to only include things that passed the filter.
	 */
	public static <T> Collection<T> filter(Iterator<T> theIterator, Predicate<T> theFilter) {
		Collection<T> aNewCollection = new ArrayList<T>();

		while (theIterator.hasNext()) {
			T aObj = theIterator.next();

			if (theFilter.accept(aObj)) {
				aNewCollection.add(aObj);
			}
		}

		return aNewCollection;
	}

	/**
	 * Transform the elements in the collection, returning a collection of new elements
	 * @param theCollection the collection of elements to transform
	 * @param theTransformer the function to use to transform the elements in the collection
	 * @param <I> the type of elements in the collection
	 * @param <O> the type the elements will be transformed to
	 * @return a collection of the elements in the source collection, transformed by the supplied function
	 */
	public static <I, O> Collection<O> transform(Collection<I> theCollection, Function<I,O> theTransformer) {
		return transform(theCollection.iterator(), theTransformer);
	}

	/**
	 * Transform the elements in the iterator, returning a collection of new elements.  null elements (elements of
	 * the original list whose transformation result is null) are not included in the list.
	 * @param theIterator the iterator of elements to transform
	 * @param theTransformer the function to use to transform the elements in the iterator
	 * @param <I> the type of elements in the iterator
	 * @param <O> the type the elements will be transformed to
	 * @return a collection of the elements in the iterator, transformed by the supplied function
	 */
	public static <I, O> Collection<O> transform(Iterator<I> theIterator, Function<I,O> theTransformer) {
		List<O> aNewCollection = new ArrayList<O>();

		while (theIterator.hasNext()) {
			O aValue = theTransformer.apply(theIterator.next());
			if (aValue != null) {
				aNewCollection.add(aValue);
			}
		}

		return aNewCollection;
	}

	/**
	 * Apply the DataCommand to each element in the collection.  Each element will be set as the data on the DataCommand
	 * prior to the command being executed.
	 * @param theCollection the collection to iterate over
	 * @param theCommand the command to execute
	 * @param <T> the type of elements in the list
	 */
	public static <T> void each(Collection<T> theCollection, DataCommand<T> theCommand) {
		for (T aElem : theCollection) {
			theCommand.setData(aElem);
			theCommand.execute();
		}
	}
	/**
	 * Apply the DataCommand to each element in the iterator.  Each element return by the iterator will be set as
	 * the data on the DataCommand prior to the command being executed.
	 * @param theIterator the iterator
	 * @param theCommand the command to execute
	 * @param <T> the type of elements in the iterator
	 */
	public static <T> void each(Iterator<T> theIterator, DataCommand<T> theCommand) {
		while (theIterator.hasNext()) {
			theCommand.setData(theIterator.next());
			theCommand.execute();
		}
	}

	/**
	 * Title: TransformingCollection<br/>
	 * Description: A collection which wraps another collection and transforms objects from the original on demand
	 * during iteration.<br/>
	 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com> <br/>
	 * Created: Oct 31, 2009 9:28:08 AM <br/>
	 *
	 * @author Michael Grove <mike@clarkparsia.com>
	 */
	private class TransformingCollection<I,O> extends AbstractCollection<O> {
		/**
		 * the original iterator
		 */
		private Collection<I> mOriginalCollection;

		/**
		 * The transforming function
		 */
		private Function<? super I, ? super O> mTransformer;

		/**
		 * Create a new TransformingCollection
		 * @param theOriginalCollection the original collection
		 * @param theTransformer the transforming function
		 */
		private TransformingCollection(final Collection<I> theOriginalCollection,
									   final Function<? super I, ? super O> theTransformer) {
			mOriginalCollection = theOriginalCollection;
			mTransformer = theTransformer;
		}

		public Iterator<O> iterator() {
			return new TransformingIterator<I,O>(mOriginalCollection.iterator(), mTransformer);
		}

		public int size() {
			return mOriginalCollection.size();
		}
	}

	/**
	 * An iterator which wraps another iterator and transforms objects from the original iterator on demand
	 * during iteration.
	 * @param <I> the type of the original objects
	 * @param <O> the type of the transformed objects
	 */
	private class TransformingIterator<I, O> implements Iterator<O> {

		/**
		 * the original iterator
		 */
		private Iterator<I> mOriginalIterator;

		/**
		 * The transforming function
		 */
		private Function<? super I, ? super O> mTransformer;

		/**
		 * Create a new TransformingIterator
		 * @param theOrig the original iterator
		 * @param theTransform the function to perform the transform
		 */
		public TransformingIterator(Iterator<I> theOrig, Function<? super I, ? super O> theTransform) {
			mOriginalIterator = theOrig;
			mTransformer = theTransform;
		}

		/**
		 * @inheritDoc
		 */
		public boolean hasNext() {
			return mOriginalIterator.hasNext();
		}

		/**
		 * @inheritDoc
		 */
		public O next() {
			// TODO: i hate generics
			return (O) mTransformer.apply(mOriginalIterator.next());
		}

		/**
		 * @inheritDoc
		 */
		public void remove() {
			mOriginalIterator.remove();
		}
	}

	/**
	 * An implementation of the Collection interface which provides a filtered view of another collection.  Operations
	 * which modify this collection also modify the original collection.
	 * @param <T>
	 */
	private class FilteredCollection<T> implements Collection<T> {

		/**
		 * The collection to filter
		 */
		private Collection<T> mUnfilteredCollection;

		/**
		 * The predicate to use to filter the original collection
		 */
		private Predicate<? super T> mPredicate;

		/**
		 * Create a new FilteredCollection
		 * @param theCollection the collection to filter
		 * @param thePredicate the predicate to use for the filtering
		 */
		public FilteredCollection(Collection<T> theCollection, Predicate<? super T> thePredicate) {
			mUnfilteredCollection = theCollection;
			mPredicate = thePredicate;
		}

		/**
		 * @inheritDoc
		 */
		public boolean add(T theElement) {
			// TODO: this does not necessarily add the element to the collection, it won't be added to the filtered view
			// if it does not pass the filter.  should we through an exception in these cases?
			return mUnfilteredCollection.add(theElement);
		}

		/**
		 * @inheritDoc
		 */
		public boolean addAll(Collection<? extends T> collection) {
			// TODO: this won't add all the elements, if there are things in what we're adding that would not
			// pass the filter, they won't get added, and that will be transparent.  we might want to consider
			// throwing a RTE for that case.
			return mUnfilteredCollection.addAll(collection);
		}

		/**
		 * @inheritDoc
		 */
		public void clear() {
			CollectionUtil.remove(mUnfilteredCollection, mPredicate);
		}

		/**
		 * @inheritDoc
		 */
		public boolean contains(Object theElement) {
			// TODO: we should catch the CCE here probably, this is not always guaranteed to be an T
			T Elem = (T) theElement;
			return mPredicate.accept(Elem) && mUnfilteredCollection.contains(theElement);
		}

		/**
		 * @inheritDoc
		 */
		public boolean containsAll(Collection<?> collection) {
			for (Object element : collection) {
				if (!contains(element)) {
					return false;
				}
			}
			return true;
		}

		/**
		 * @inheritDoc
		 */
		public boolean isEmpty() {
			return size() == 0;
		}

		/**
		 * @inheritDoc
		 */
		public Iterator<T> iterator() {
			return new FilteredIterator<T>(mUnfilteredCollection.iterator(), mPredicate);
		}

		/**
		 * @inheritDoc
		 */
		public boolean remove(Object element) {
			// TODO: we should catch the CCE here probably, this is not always guaranteed to be an T
			T e = (T) element;
			return mPredicate.accept(e) && mUnfilteredCollection.remove(element);
		}

		/**
		 * @inheritDoc
		 */
		public boolean removeAll(final Collection<?> theCollection) {
			boolean aRemoved = false;
			for (final Object aObj : theCollection) {
				aRemoved = true;
				remove(aObj);
			}

			return aRemoved;
		}

		/**
		 * @inheritDoc
		 */
		public boolean retainAll(final Collection<?> theCollection) {
			boolean aRemoved = false;
			for (final Object aObj : theCollection) {
				if (!contains(aObj)) {
					aRemoved = true;
					remove(aObj);
				}
			}

			return aRemoved;
		}

		/**
		 * @inheritDoc
		 */
		public int size() {
			return CollectionUtil.size(iterator());
		}

		/**
		 * @inheritDoc
		 */
		public Object[] toArray() {
			return list(iterator()).toArray();
		}

		/**
		 * @inheritDoc
		 */
		public <E> E[] toArray(E[] theArray) {
			return list(iterator()).toArray(theArray);
		}

		/**
		 * @inheritDoc
		 */
		@Override
		public String toString() {
			return "[ " + BasicUtils.join(", ", this) + " ]";
		}
	}

	public class FilteredIterator<T> implements Iterator<T> {
		private Iterator<T> mIter;
		private Predicate<? super T> mPredicate;
		private T mNext;

		public FilteredIterator(final Iterator<T> theIter, final Predicate<? super T> thePredicate) {
			mIter = theIter;
			mPredicate = thePredicate;
		}

		public boolean hasNext() {
			return mIter.hasNext() && findNext();

		}

		private boolean findNext() {
			if (mNext != null) {
				return true;
			}
			else {
				while (mIter.hasNext()) {
					T aObj = mIter.next();
					if (mPredicate.accept(aObj)) {
						mNext = aObj;
						return true;
					}
				}

				return false;
			}
		}

		public T next() {
			if (mNext == null) {
				findNext();
			}

			if (mNext == null) {
				throw new NoSuchElementException();
			}

			T aNext = mNext;
			mNext = null;

			return aNext;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}