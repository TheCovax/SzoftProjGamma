package Fungorium.src.model;

import Fungorium.src.model.tekton.Tekton;
import Fungorium.src.utility.Logger;

/**
 * A GombaFonal osztály egy gombafonal kapcsolatot reprezentál két Tekton között.
 * A gombafonalakon át a rovarok közlekedhetnek a tektonok között.
 * Minden gombafonal rendelkezik egy tulajdonossal (owner), amely az adott játékoshoz vagy irányítóhoz kapcsolható.
 */
public class GombaFonal {
	
	Tekton src;	
	Tekton dst;
	String owner;

	/**
	 * Konstruktor a GombaFonal létrehozásához.
	 * @param s A forrás Tekton.
	 * @param d A cél Tekton.
	 * @param o A gombafonal tulajdonosának neve.
	 */
	public GombaFonal(Tekton s, Tekton d, String o){
		src = s;
		dst = d;
		owner = o;
	}

	/**
	 * Eltávolítja a gombafonalat a kapcsolódó tektonokból.
	 */
	public void clean(){
		if(src != null) src.removeGombaFonal(this);
		if(dst != null) dst.removeGombaFonal(this);
	}

	public boolean isOwner(String s){
		return owner.equals(s);
	}

	/**
	 * Visszaadja a másik végén lévő Tekton-t.
	 * @param current A jelenlegi Tekton.
	 * @return A másik végén található Tekton.
	 * @throws IllegalArgumentException ha a megadott Tekton nem kapcsolódik ehhez a gombafonalhoz.
	 */
	public Tekton getOtherEnd(Tekton current){
		if (current.equals(src)) {
			return dst;
		} else if (current.equals(dst)) {
			return src;
		} else {
			throw new IllegalArgumentException("The given tekton is not associated with this Gombafonal");
		}
	}

}
