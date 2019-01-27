package com.sy.mingding.Bean;

public class Todo {

    /** TODO名 */
    private String todoName;
    /** 项目ID */
    private String projectId ;




    /** TODO名 */
    public String getTodoName(){
        return this.todoName;
    }
    /** TODO名 */
    public void setTodoName(String todoName){
        this.todoName = todoName;
    }
    /** 项目ID */
    public String getProjectId(){
        return this.projectId;
    }
    /** 项目ID */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
