package Fungorium.src.model;

//import java.lang.runtime.TemplateRuntime;
import Fungorium.src.model.player.Gombasz;
import Fungorium.src.model.player.Player;
import Fungorium.src.model.tekton.Tekton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A GombaFonal osztály egy gombafonal kapcsolatot reprezentál két Tekton között.
 * A gombafonalakon át a rovarok közlekedhetnek a tektonok között.
 * Minden gombafonal rendelkezik egy tulajdonossal (owner), amely az adott játékoshoz vagy irányítóhoz kapcsolható.
 */
public class GombaFonal extends Entity{
	private static final AtomicInteger generatedCounter = new AtomicInteger(0);
	Tekton src;
	Tekton dst;
	int roundsToDestruction;
	boolean scheduledForDestruction;
	double eatParalyzedRovarRate;

	/**
	 * Konstruktor a GombaFonal létrehozásához.
	 * @param s A forrás Tekton.
	 * @param d A cél Tekton.
	 * @param o A gombafonal tulajdonosának neve.
	 */
	public GombaFonal(Tekton s, Tekton d, Player o){
        super(o);
        src = s;
		dst = d;
		eatParalyzedRovarRate = 0.5;
	}

	public GombaFonal(String id, Tekton s, Tekton d, Player o){
		super(id, o);
		src = s;
		dst = d;
		eatParalyzedRovarRate = 0.5;
	}

	public boolean isOwner(Player p){
		return owner.equals(p);
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

	/**
	 * Beíllitja, hogy mennyi ido mulva semmisuljon meg a gombafonal és jelzi, hogy meg fog semmisulni
	 * @param time a beallitando ido
	 * @return sikerult-e a beallitas
	 */
	public boolean scheduleDestruction(int time){
		
		if(scheduledForDestruction) return false;

		roundsToDestruction = time;
		scheduledForDestruction = true;
		return true;
	}

	/**
	 * Adott esellyej megeszi a benult rovarokat, minden korben no az esely
	 */
	void eatParalyzedRovar(){
		if(shouldEatRovar()){
		List<Rovar> rovar = paralysedRovar(src);
		if(rovar != null ) killRovar(rovar);

		rovar = paralysedRovar(dst);
		if(rovar != null) killRovar(rovar);
		}
	}

	
	/**Eltavolit egy adott rovarok listajaat az tektonjaikrol
     * @param r Azon rovarok listaja, amit meg fog olni
	 * 
     */
	void killRovar(List<Rovar> r){

		//Minden benult rovaron vegigmegy
		for (Rovar rovar : r) {
			//A tektonon levo rovarok listaja
			List<Rovar> rovarok = rovar.getTekton().getRovarok();
			//Eltavolitas a rovarok listajabol
			rovarok.remove(rovarok.indexOf(rovar));
		}


	}

	/**Megadja az adott tektonon levo lebenintott rovarrokat,
	*ha nincs egy se, null-t ad
	*@param t Az atnezendo tekton
	*/
	List<Rovar> paralysedRovar(Tekton t){
		List<Rovar> paralysed = new ArrayList<>();
		for (Rovar r : t.getRovarok()) {
			if(r.isParalyzed()) paralysed.add(r);
		}

		if(paralysed.isEmpty()) return paralysed;
		else return null;
	}


	/**
	 * Megadja hogy a korben egye a benult rovart, vagy sem
	 */
	boolean shouldEatRovar(){

		//Random szam 0 és 1 között, amire: 0 < szam < 1
		double splitCheck = ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, 1.0);

		//Ha az elobb generalt szam kisebb, mint az eatParalyzedRovarRate igazat ad vissza
		//Igy a 0.0 splitRate garantaltan hamis lesz es az 1.0 splitRate pedig biztosan igaz lesz
		//Ha nem sikerul, noveli az eselyt
		if(splitCheck < eatParalyzedRovarRate) return true;
		else{
			eatParalyzedRovarRate += 0.1;
			return false;
		} 
	}

	@Override
	protected String getPrefix() {
		return "F";
	}

	/**
	 * Frissiti a gombafonal allapotat
	 */
	@Override
	public void update(){
		eatParalyzedRovar();

		if (scheduledForDestruction){
			roundsToDestruction--;
			if (roundsToDestruction<=0){
				this.delete();
			}
		}
	}

	@Override
	public void delete() {
		if(src != null) src.removeGombaFonal(this);
		if(dst != null) dst.removeGombaFonal(this);
		((Gombasz) owner).removeFonal(this);

	}

	public int getRoundsToDestruction() {
        return roundsToDestruction;
    }

	@Override
	public String toString() {
		return "GombaFonal{" +
				"ID='" + getID() + '\'' +
				", Owner=" + (owner != null ? owner.getName() : "None") +
				", SrcTekton=" + (src != null ? src.getID() : "None") +
				", DstTekton=" + (dst != null ? dst.getID() : "None") +
				'}';
	}
}
