package Fungorium.src.model.tekton;

import java.util.List;

public class TermekenyTekton extends Tekton {

        /**
		 * splitRate es gombaTest beallito konstruktor
		 */
		public TermekenyTekton(double splitR, List<Tekton> neighborsList){
			super(splitR, neighborsList);
		}

        public TermekenyTekton(){
			super();
		}

		public TermekenyTekton(String id){
		super(id);
	}
}
