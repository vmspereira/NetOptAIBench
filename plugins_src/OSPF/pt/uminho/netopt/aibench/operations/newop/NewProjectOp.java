/**
* Copyright 2012-2017,
* Centro Algoritmi
* University of Minho
*
* This is free software: you can redistribute it and/or modify
* it under the terms of the GNU Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This code is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Public License for more details.
*
* You should have received a copy of the GNU Public License
* along with this code.  If not, see <http://www.gnu.org/licenses/>.
* 
* @author Vítor Pereira
*/
package pt.uminho.netopt.aibench.operations.newop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetGraph;
import pt.uminho.algoritmi.netopt.ospf.utils.io.GraphReader;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;

@Operation(name= "New Project", description = "Creates a new project for a given network topology.\nThe network can be build from a set of nodes and a set of edges, from a GML file or from a GraphGML file.")
public class NewProjectOp {

	private File fileNodes;
	private File fileEdges;
	private File graphMLFile;
	private File GMLFile;
	

	@Port(direction = Direction.INPUT, name = "Nodes file: ", order = 1, allowNull = true)
	public void setFileNodes(File in) {
		this.fileNodes = in;
	}

	@Port(direction = Direction.INPUT, name = "Edges file: ", order = 2, allowNull = true)
	public void setFileEdges(File in) {
		this.fileEdges = in;
	}

	@Port(direction = Direction.INPUT, name = "GML file (JSON): ", order = 3, allowNull = true)
	public void setGML(File in) {
		this.GMLFile = in;
	}
	
	@Port(direction = Direction.INPUT, name = "GraphML file (XML): ", order = 4, allowNull = true)
	public void setGraphML(File in) {
		this.graphMLFile = in;
	}

	@Port(direction = Direction.OUTPUT, order = 100)
	public ProjectBox run() throws IOException {
		ProjectBox p = null;
		if (fileNodes.exists() && fileEdges.exists()) {
			p = new ProjectBox(fileNodes.getAbsolutePath(), fileEdges.getAbsolutePath());
			p.setName(fileNodes.getName().substring(0, fileNodes.getName().lastIndexOf('.')));
			return p;
		}else if (GMLFile.exists()) {
			FileInputStream fis = new FileInputStream(GMLFile);
			NetGraph graph = GraphReader.readGML(fis);
			p = new ProjectBox(graph);
			p.setName(GMLFile.getName().substring(0, GMLFile.getName().lastIndexOf('.')));
			return p;
		}else if (graphMLFile.exists()) {
			FileInputStream fis = new FileInputStream(graphMLFile);
			NetGraph graph = GraphReader.readGraphML(fis);
			p = new ProjectBox(graph);
			p.setName(graphMLFile.getName().substring(0, graphMLFile.getName().lastIndexOf('.')));
			return p;
		}else {
			return p;
		}	
	}

}
