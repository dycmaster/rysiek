package dycmaster.rysiek.shared;


import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Create {

	public static Set newSet(){
		return new HashSet();
	}

	public static Collection newCollection(){
		return  new LinkedList();
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
