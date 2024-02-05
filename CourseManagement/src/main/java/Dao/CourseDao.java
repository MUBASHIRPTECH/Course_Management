package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DbConnect;
import model.Course;

public class CourseDao {

    Connection connection;

    public CourseDao() throws ClassNotFoundException, SQLException {
        connection = DbConnect.getConnection();
    }

    public void addCourse(Course course) {
        try {            
            PreparedStatement pst = connection.prepareStatement("INSERT INTO course(coursename,fees,category,duration) VALUES (?,?,?,?)");
            pst.setString(1, course.getCoursename());
            pst.setInt(2, course.getFees());
            pst.setString(3, course.getCategory());
            pst.setInt(4, course.getDuration());            
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<Course>();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery("SELECT * FROM course");

            while (rs.next()) {
                Course course = new Course();
                course.setCoursename(rs.getString("coursename"));
                course.setCourseid(rs.getInt("courseid"));
                course.setCategory(rs.getString("category"));
                course.setFees(rs.getInt("fees"));
                course.setDuration(rs.getInt("duration"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    
    public Course getCourseByID(int courseid) {
        Course course = null;
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM course WHERE courseid=?");
            pst.setInt(1, courseid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                course = new Course();
                course.setCoursename(rs.getString("coursename"));
                course.setCourseid(rs.getInt("courseid"));
                course.setCategory(rs.getString("category"));
                course.setFees(rs.getInt("fees"));
                course.setDuration(rs.getInt("duration"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }
    
    public void updateCourse(Course course) {
    	
    	try {
            PreparedStatement pst = connection.prepareStatement("update course set " + "coursename=? , category=? , fees=? , duration=?" + " where courseid=?");
            pst.setString(1, course.getCoursename()); 
            pst.setString(2, course.getCategory());
            pst.setInt(3, course.getFees());
            pst.setInt(4, course.getDuration());  
            pst.setInt(5, course.getCourseid()); 
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    
    public void deleteCourse(int courseid) {
        try {
            PreparedStatement pst = connection.prepareStatement("DELETE FROM course WHERE courseid=?");
            pst.setInt(1, courseid);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
