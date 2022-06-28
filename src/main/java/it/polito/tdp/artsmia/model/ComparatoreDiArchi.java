package it.polito.tdp.artsmia.model;

import java.util.Comparator;

public class ComparatoreDiArchi implements Comparator<Arco> {

	@Override
	public int compare(Arco o1, Arco o2) {
	
		return (int) -(o1.getPeso()-o2.getPeso());
	}

}
