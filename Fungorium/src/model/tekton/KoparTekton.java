package Fungorium.src.model.tekton;

import java.util.List;

public class KoparTekton extends Tekton {

    	/**
		 * splitRate es gombaTest beallito konstruktor
		 */
		public KoparTekton(double splitR, List<Tekton> neighborsList){
			super(splitR, neighborsList);
		}

        public KoparTekton(){
			super();
		}

		public KoparTekton(String id){
		super(id);
	}

		
}
