package dycmaster.rysiek.shared;


import java.util.*;

public class Create {

	public static <T> Set<T> newSet(){
		return new HashSet<>();
	}

	public static <T> Collection<T> newCollection(){
		return  new LinkedList<>();
	}

    public static <T> List<T> newList(){
        return  new LinkedList<>();
    }

	public static <E> Set<E> boxIntoSet(E elt, E... elts){
		Set<E> newSet = Create.newSet();
		newSet.add(elt);
		for(E e: elts){
			newSet.add(e);
		}
		return  newSet;
	}

}
