package Fungorium.src.model.tekton;

import Fungorium.src.model.GombaFonal;
import Fungorium.src.model.GombaTest;
import Fungorium.src.model.Rovar;
import Fungorium.src.model.player.Player;
import Fungorium.src.model.spora.Spora;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Tekton {
	private static final AtomicInteger idCounter = new AtomicInteger(0);
	private static final Set<String> usedIds = new HashSet<>();
	private final String id;

	Queue<Spora> sporak;
	List<GombaFonal> fonalak;

	List<Tekton> neighbours;
	List<GombaTest> test;
	List<Rovar> rovarok;
	double splitRate;
	boolean isConnected;

	/**
	 * Parameter nelkuli konstruktor
	 */
	public Tekton(){
		sporak = new LinkedList<>();
		fonalak = new ArrayList<>();
		neighbours = new ArrayList<>();
		test = new ArrayList<>();
		rovarok = new ArrayList<>();
		splitRate = 0.5;
		id = generateAutoId();
	}

	/**
	 * Parameter nelkuli konstruktor
	 */
	public Tekton(String id){
		sporak = new LinkedList<>();
		fonalak = new ArrayList<>();
		neighbours = new ArrayList<>();
		test = new ArrayList<>();
		rovarok = new ArrayList<>();
		splitRate = 0.5;
		this.id = id;
		registerExplicitId(id);
	}

	/**
	 * splitRate es gombaTest beallito konstruktor (elsosorban a tesztekhez)
	 */
	public Tekton(double splitR, GombaTest g){
		sporak = new LinkedList<>();
		fonalak = new ArrayList<>();
		neighbours = new ArrayList<>();
		test = new ArrayList<>();
		test.add(g);
		rovarok = new ArrayList<>();
		splitRate = splitR;

		id = "T" + idCounter.incrementAndGet();
	}

	/**
	 * splitRate es gombaTest beallito konstruktor
	 */
		public Tekton(double splitR, List<Tekton> neighboursList){
			sporak = new LinkedList<>();
			fonalak = new ArrayList<>();
			neighbours = neighboursList;
			test = new ArrayList<>();
			rovarok = new ArrayList<>();
			splitRate = splitR;

			id = "T" + idCounter.incrementAndGet();
		}

	public List<GombaTest> getTestek(){
		return test;
	}

	public List<GombaFonal> getFonalak() {
		return getFonalak(null);
	}

	/// Visszaadja a tektonon lévő gombafonalakat, ha a tulajdonos null, akkor minden fonalat visszaad
	/// Ha a tulajdonos nem null, akkor csak azokat a fonalakat adja vissza, amelyek a megadott tulajdonoshoz tartoznak
	public List<GombaFonal> getFonalak(Player owner) {
		List<GombaFonal> loc_fonalak = new ArrayList<>();
		for (GombaFonal fonal : this.fonalak) {
			if (owner == null || fonal.isOwner(owner)) {
				loc_fonalak.add(fonal);
			}
		}
		return loc_fonalak;
	}

	public Queue<Spora> getSporak(){
		return sporak;
	}

	public void growFonal(Tekton dst, Player owner){
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
		else System.out.println("This fonal could not be grown");
		
	}

	public void addGombaTest(GombaTest gt) {
		test.add(gt);
	}


	public double getSplitRate() {
		return splitRate;
	}

	public void setSplitRate(double splitRate) {
		this.splitRate = splitRate;
	}

	public List<GombaTest> getGombaTestek() {
		return test;
	}

	public void addSpora(Spora s, Player p){
		sporak.add(s);
		checkForGombatest(p);
	}

	public Spora removeSpora(){
		return sporak.poll();
	}

	public Spora peekSpora(){return sporak.peek();}

	public void addGombaFonal(GombaFonal f){
		fonalak.add(f);
		checkForGombatest(f.getOwner());
	}

	void checkForGombatest(Player p){
		if(!(this instanceof KoparTekton)){
			for (GombaFonal f : fonalak) {
				if(sporak.size() >= 3 && f.isOwner(p)) addGombaTest(new GombaTest(this, p));
			}

		}
	}

	/**
	 * Kettetori a tektont
	 */
	public Tekton split(){

		//Letrehoz egy uj listat a szomszedikrol, amiben onmaga is szerepel
		List<Tekton> newNeighbors = new ArrayList<>(neighbours);
		newNeighbors.add(this);

		//Csokkenti a split rate-t, hogy legkozelebb kisebb esellyel torjon el
		if(splitRate >0.1) splitRate -= 0.1;
		


		//Letrehoz egy uj tektont, a csokkentett splitRate-el és az elobb letrehozott uj szomszedokkal

		Tekton newTekton = null;

		int newTektonType;

		//Ha mar nem torhet tovabb a tekton, automatikusan stabil lesz, egyebkent random a tipusa
		if(splitRate <= 0) newTektonType = 0;
		else newTektonType = ThreadLocalRandom.current().nextInt(0, 5);

		switch (newTektonType) {
			case 0 -> newTekton = new StabilTekton(splitRate, newNeighbors);
			case 1 -> newTekton = new ElszigeteltTekton(splitRate, newNeighbors);
			case 2 -> newTekton = new HosztilisTekton(splitRate, newNeighbors);
			case 3 -> newTekton = new KoparTekton(splitRate, newNeighbors);
			case 4 -> newTekton = new TermekenyTekton(splitRate, newNeighbors);
			default -> {
                }
		}

		//A jelenlegi tektonrol torli a fonalakat es a gombatestet;
		fonalak.clear();
		test.clear();


		//Hozzaadja a jelenlegi tekton szomszedjaihoz az ujjjonnan letrehozottat
		neighbours.add(newTekton);

		return newTekton;
	}

	/**
	 * Meghatarozza, hogy egy tekton eltorjon-e az adott korben
	 */
	public boolean canSplit(){

		//Random szam 0 és 1 között, amire: 0 < szam < 1
		double splitCheck = ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, 1.0);

		//Ha az elobb generalt szam kisebb, mint a splitRate igazat ad vissza
		//Igy a 0.0 splitRate garantaltan nem torik el es az 1.0 splitRate pedig biztosan elfog
		if(splitRate > splitCheck) return true;
		else return false;
	}

	/**
	 * Eltavolit egy adott gombafonalat
	 */
	public void removeGombaFonal(GombaFonal f){
		fonalak.remove(f);
	}

	/**
	 * Megadja, hogy egy adott gombafonal a tektonon van-e
	 */
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

	public List<Tekton> checkConnectivity(Player owner) {
		List<GombaFonal> loc_fonalak = this.getFonalak(owner);
		
		List<Tekton> targets = new ArrayList<>();

            while (!loc_fonalak.isEmpty()){
				GombaFonal fonal = loc_fonalak.get(0);
				Tekton candidate = fonal.getOtherEnd(this);
				if(!targets.contains(candidate)){
					targets.add(candidate);
					candidate.getFonalak(owner);
				}
				loc_fonalak.remove(fonal);
            } 

		return targets;
	}

	public void setIsConnected(boolean b) {
		isConnected = b;
	}

	public boolean getIsConnected(){ return isConnected;}


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

		return reachable;
	}

	public void update(){
		if(canSplit()) split();
	}

	public void removeGombatest(GombaTest t){
		test.remove(test.indexOf(t));
	}

	public List<Rovar> getRovarok(){
		return rovarok;
	}

	public String getID(){
		return id;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" { ");
		sb.append("ID='").append(getID()).append('\'');

		sb.append(", Neighbours=[");
		for (int i = 0; i < neighbours.size(); i++) {
			sb.append(neighbours.get(i).getID());
			if (i < neighbours.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");

		sb.append(", IsConnected=").append(isConnected);
		sb.append(", SplitRate=").append(splitRate);

		// list GombaTestek
		sb.append(", GombaTestek=[");
		for (int i = 0; i < test.size(); i++) {
			sb.append(test.get(i).getID());
			if (i < test.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");

		// list Rovarok
		sb.append(", Rovarok=[");
		for (int i = 0; i < rovarok.size(); i++) {
			sb.append(rovarok.get(i).getID());
			if (i < rovarok.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");

		// list GombaFonalak
		sb.append(", GombaFonalak=[");
		for (int i = 0; i < fonalak.size(); i++) {
			sb.append(fonalak.get(i).getID());
			if (i < fonalak.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");

		// write Spora number
		sb.append(", SporakCount=").append(sporak.size());

		sb.append(" }");
		return sb.toString();
	}

	public List<Tekton> getNeighbours() {
		return neighbours;
	}

	private static String registerExplicitId(String id) {
		if (usedIds.contains(id)) {
			throw new IllegalStateException("Duplicate ID: " + id);
		}
		usedIds.add(id);
		return id;
	}

	protected String generateAutoId() {
		String newId;
		do {
			newId = "T" + idCounter.incrementAndGet();
		} while (usedIds.contains(newId)); // 确保唯一
		usedIds.add(newId);
		return newId;
	}

}
