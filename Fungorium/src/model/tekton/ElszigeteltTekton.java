package Fungorium.src.model.tekton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Fungorium.src.model.GombaFonal;

public class ElszigeteltTekton extends Tekton{

		/**
		 * splitRate es gombaTest beallito konstruktor
		 */
		public ElszigeteltTekton(double splitR, List<Tekton> neighborsList){
			super(splitR, neighborsList);
		}


		public ElszigeteltTekton(){
			super();
		}

		/**
		 * Amellett, hogy teszteli a spora szamot es hogy a cel tektonra tud-e noni
		 * azt is teszteli, hogy a jelenlegin van-e meg hely
		 * @param dst cel tekton
		 * @param owner tulajdonos jatekos
		 */
		@Override
		public void growFonal(Tekton dst, String owner){
			
			if(this.sporak.size() >= 2 && (!(dst instanceof ElszigeteltTekton) || dst.fonalak.isEmpty()) && fonalak.isEmpty()){

			//Letrehozza az uj fonalat és hozzaadja mindket tekton fonalak listajahoz
			sporak.poll();
			sporak.poll();

			GombaFonal ujFonal = new GombaFonal(this, dst, owner);

			fonalak.add(ujFonal);
			dst.addGombaFonal(ujFonal);

		}
		} 
}
