package Fungorium.src.model.tekton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import Fungorium.src.model.GombaFonal;
import Fungorium.src.model.GombaTest;
import Fungorium.src.model.Rovar;
import Fungorium.src.model.spora.Spora;

public class Tekton {

	Queue<Spora> sporak;
	List<GombaFonal> fonalak;
	List<Tekton> neighbors;
	List<GombaTest> test;
	List<Rovar> rovarok;
	double splitRate;



	//Parameter nelkuli konstruktor
	public Tekton(){
		sporak = new LinkedList<>();
		fonalak = new ArrayList<>();
		neighbors = new ArrayList<>();
		test = new ArrayList<>();
		rovarok = new ArrayList<>();
		splitRate = 0.5;
	}

	//splitRate es gombaTest beallito konstruktor (elsosorban a tesztekhez)
	public Tekton(double splitR, GombaTest g){
		sporak = new LinkedList<>();
		fonalak = new ArrayList<>();
		neighbors = new ArrayList<>();
		test = new ArrayList<>();
		test.add(g);
		rovarok = new ArrayList<>();
		splitRate = splitR;
	}

		//splitRate es gombaTest beallito konstruktor
		public Tekton(double splitR, List<Tekton> neighborsList){
			sporak = new LinkedList<>();
			fonalak = new ArrayList<>();
			neighbors = neighborsList;
			test = new ArrayList<>();
			rovarok = new ArrayList<>();
			splitRate = splitR;
		}

	public List<GombaFonal> getFonalak(){
		return fonalak;
	}

	public Queue<Spora> getSporak(){
		return sporak;
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

	public void addGombaFonal(GombaFonal f){
		fonalak.add(f);
	}

	//Kettetori a tektont
	public Tekton split(){

		//Letrehoz egy uj listat a szomszedikrol, amiben onmaga is szerepel
		List<Tekton> newNeighbors = new ArrayList<>(neighbors);
		newNeighbors.add(this);

		//Csokkenti a split rate-t, hogy legkozelebb kisebb esellyel torjon el
		splitRate -= 0.1;


		//Letrehoz egy uj tektont, a csokkentett splitRate-el és az elobb letrehozott uj szomszedokkal
		
		Tekton newTekton = null;

		int newTektonType;

		//Ha mar nem torhet tovabb a tekton, automatikusan stabil lesz, egyebkent random a tipusa
		if(splitRate <= 0) newTektonType = 0;
		else newTektonType = ThreadLocalRandom.current().nextInt(0, 5);
		
		switch (newTektonType) {
			case 0:
				newTekton = new StabilTekton(splitRate, newNeighbors); 
				break;
			case 1:
				newTekton = new ElszigeteltTekton(splitRate, newNeighbors); 
					break;
			case 2:
				newTekton = new HosztilisTekton(splitRate, newNeighbors); 
					break;
			case 3:
				newTekton = new KoparTekton(splitRate, newNeighbors); 
					break;
			case 4:
				newTekton = new TermekenyTekton(splitRate, newNeighbors); 
					break;
			default:
				break;
		}

		//A jelenlegi tektonrol torli a fonalakat es a gombatestet;
		fonalak.clear();
		test.clear();


		//Hozzaadja a jelenlegi tekton szomszedjaihoz az ujjjonnan letrehozottat
		neighbors.add(newTekton);

		return newTekton;
	}

	//Meghatarozza, hogy egy tekton eltorjon-e az adott korben
	public boolean canSplit(){

		//Random szam 0 és 1 között, amire: 0 < szam < 1
		double splitCheck = ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, 1.0);

		//Ha az elobb generalt szam kisebb, mint a splitRate igazat ad vissza
		//Igy a 0.0 splitRate garantaltan nem torik el es az 1.0 splitRate pedig biztosan elfog
		if(splitRate > splitCheck) return true;
		else return false; 
		
	}
}
