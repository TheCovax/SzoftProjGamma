package Fungorium.src.model.tekton;

import Fungorium.src.model.GombaTest;
import java.util.List;

public class StabilTekton extends Tekton {

    	/**
		 * splitRate es gombaTest beallito konstruktor
		 */
		public StabilTekton(double splitR, List<Tekton> neighborsList){
			super(splitR, neighborsList);
		}

        public StabilTekton(double splitR, GombaTest g){
			super(splitR, g);
		}

        public StabilTekton(){
			super();
		}
}
