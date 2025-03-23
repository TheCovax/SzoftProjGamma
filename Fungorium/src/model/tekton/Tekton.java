package Fungorium.src.model.tekton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Fungorium.src.model.GombaFonal;
import Fungorium.src.model.spora.Spora;

public class Tekton {

	Queue<Spora> sporak;
	List<GombaFonal> fonalak;

	//Konstruktor igeny szerint ki kell egesziteni
	public Tekton(){
		sporak = new LinkedList<>();
		fonalak = new ArrayList<>();
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

	public void addGombaFonal(GombaFonal f){
		fonalak.add(f);
	}

}
