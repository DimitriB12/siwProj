package it.uniroma3.siw.siwProj.model;

public class ContenitoreStringhe {
	
	public String nameProject;
	
	public String userName;
	
	public ContenitoreStringhe() {}
	
	public ContenitoreStringhe(String nameProject, String nameUser) {
		
		this.nameProject = nameProject;
		this.userName = nameUser;
	}

	public String getNameProject() {
		return nameProject;
	}

	public void setNameProject(String nameProject) {
		this.nameProject = nameProject;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nameProject == null) ? 0 : nameProject.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContenitoreStringhe other = (ContenitoreStringhe) obj;
		if (nameProject == null) {
			if (other.nameProject != null)
				return false;
		} else if (!nameProject.equals(other.nameProject))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	


}
