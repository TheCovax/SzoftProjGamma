package Fungorium.src.model.tekton;

import java.util.*;

import Fungorium.src.model.GombaFonal;
import Fungorium.src.model.Rovar;
import Fungorium.src.model.spora.Spora;
import Fungorium.src.utility.Logger;

public class Tekton {

	Queue<Spora> sporak;
	List<GombaFonal> fonalak;
	List<Tekton> neighbours;
	List<Rovar> rovarok;

	//Konstruktor igeny szerint ki kell egesziteni
	public Tekton(){
		sporak = new LinkedList<>();
		fonalak = new ArrayList<>();
		neighbours = new ArrayList<>();
		rovarok = new ArrayList<>();
	}

	public List<GombaFonal> getFonalak(){
		return fonalak;
	}

	public void growFonal(Tekton dst, String owner){
		//Ellenorzi, hogy a jelenlegi tektonon van-e legalább 2 spora 
		//és rafer-e a celtektonra az uj fonal (ha ElszigeteletTekton, uresnek kell lennie)
		if(this.sporak.size() >= 2 && (!(dst instanceof ElszigeteltTekton) || dst.fonalak.isEmpty())){

			//Letrehozza az uj fonalat és hozzaadja mindket tekton fonalak listajahoz
			sporak.poll();
			sporak.poll();

			GombaFonal ujFonal = new GombaFonal(this, dst, owner);

			fonalak.add(ujFonal);
			dst.addGombaFonal(ujFonal);

		}
	}

	public void addSpora(Spora s){
		sporak.add(s);
	}

	public Spora removeSpora(){
		return sporak.poll();
	}

	public Spora peekSpora(){return sporak.peek();}

	public void addGombaFonal(GombaFonal f){
		fonalak.add(f);
	}

	public void removeGombaFonal(GombaFonal f){
		fonalak.remove(f);
	}

	public boolean hasGombafonal(GombaFonal gf){ return fonalak.contains(gf);}

	public void addNeighbour(Tekton t){
		neighbours.add(t);
	}

	public void removeNeighbour(Tekton t){
		neighbours.remove(t);
	}

	public void addRovar(Rovar r){
		rovarok.add(r);
	}

	public void removeRovar(Rovar r) {
		rovarok.remove(r);
	}

	/**
	 * Visszadja az összes elérhető Tekton-t a jelenlegi tektonból kiindulva (elérhető: össze vannak kapcsolódva gombafonalakkal),
	 * adott sebesség (lépés-távolság) korlátozásával.
	 * <p>Az algoritmus BFS (szélességi keresés) segítségével bejárja a tektonokat,
	 * amelyek gombafonallal kapcsolódnak az aktuális tektonhoz, majd rekurzívan
	 * folytatja a keresést a következő szinteken, amíg a sebességhatárt el nem éri.</p>
	 *
	 * @param speed A keresés mélysége, azaz a maximálisan bejárható távolság.
	 * @return Egy lista a jelenlegi tektonból elérhető tektonokról.
	 */
	public List<Tekton> findReachableTektonWithinDistance(int speed) {
		Logger.methodCall("tekton.findReachableTektonWithinDistance(speed)");

		List<Tekton> reachable = new ArrayList<>();
		Queue<Tekton> queue = new LinkedList<>();
		Set<Tekton> visited = new HashSet<>();

		queue.add(this);
		visited.add(this);

		int depth = 0;

		while (!queue.isEmpty() && depth < speed){
			int levelSize = queue.size();
			for (int i = 0; i < levelSize; i++) {
				Tekton current = queue.poll();

				for (GombaFonal gf : current.getFonalak()){
					Tekton connectedTekton = gf.getOtherEnd(current);
					if(!visited.contains(connectedTekton)){
						reachable.add(connectedTekton);
						queue.add(connectedTekton);
						visited.add(connectedTekton);
					}
				}
			}
			depth++;
		}

		Logger.methodReturn("tekton.findReachableTektonWithinDistance(speed)");
		return reachable;
	}
}
