package dycmaster.rysiek.shared;

import java.util.*;

public class Create {

	public static <T> Set<T> newSet(){
		return new HashSet<>();
	}

	public static <T> Collection<T> newCollection(){
		return  new LinkedList<>();
	}

    public static <T> Collection<T> newCollection( T... array){
        Collection res = newCollection();
        for (T t : array) {
            res.add(t);
        }
        return  res;
    }

    public static <T> List<T> newList( T... array){
        List res = newList();
        for (T t : array) {
            res.add(t);
        }
        return  res;
    }

    public static <T,K> Map<T,K> newMap(){
        return  new HashMap<>();
    }

    public static <T> List<T> newList(){
        return  new LinkedList<>();
    }


    @SafeVarargs
    public static <E> Set<E> boxIntoSet(E elt, E... elts){
		Set<E> newSet = Create.newSet();
		newSet.add(elt);
        newSet.addAll(Arrays.asList(elts));
		return  newSet;
	}

}
