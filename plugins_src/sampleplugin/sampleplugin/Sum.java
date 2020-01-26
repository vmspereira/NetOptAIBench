/*******************************************************************************
 * /**
 * * Copyright 2012-2017,
 * * Centro Algoritmi
 * * University of Minho
 * *
 * * This is free software: you can redistribute it and/or modify
 * * it under the terms of the GNU Public License as published by
 * * the Free Software Foundation, either version 3 of the License, or
 * * (at your option) any later version.
 * *
 * * This code is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * * GNU Public License for more details.
 * *
 * * You should have received a copy of the GNU Public License
 * * along with this code.  If not, see <http://www.gnu.org/licenses/>.
 * * 
 * * @author Vítor Pereira
 * */
 ******************************************************************************/
package sampleplugin;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port; 

@Operation(description="This is a sample operation that adds two numbers.")
public class Sum{

	private int x,y;
	
	@Port(direction=Direction.INPUT, name="x param", order=1)
	public void setX(int x){
		this.x = x;
	}

	@Port(direction=Direction.INPUT, name="y param", order=2)
	public void setY(int y){
		this.y = y;
	}


	@Port(direction=Direction.OUTPUT, order=3)
	public int sum(){
		return this.x + this.y;
	}

}
