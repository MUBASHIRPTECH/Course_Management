package web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import Dao.CourseDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/CourseServlet")
public class CourseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CourseDao dao;

    public CourseServlet() {
        super();
        try {
            dao = new CourseDao();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            String forward = "/view.jsp"; // Assuming view.jsp is in the root directory of your webapp

            if (action == null) {
                action = "default";
            }

            switch (action) {
                case "delete":
                    int courseid = Integer.parseInt(request.getParameter("courseid"));
                    dao.deleteCourse(courseid);
                    
                    RequestDispatcher del = request.getRequestDispatcher(forward);
                    request.setAttribute("courses", dao.getAllCourses());
                    del.forward(request, response);
                    
                    break;

                case "edit":
                   // System.out.println(action);
                	RequestDispatcher edit = request.getRequestDispatcher("add.jsp");
                	
                	courseid =Integer.parseInt(request.getParameter("courseid"));
                	
                	Course course=dao.getCourseByID(courseid);
                	request.setAttribute("course", course);
                	
                	edit.forward(request, response);
                	
                    break;

                default:
                   RequestDispatcher view = request.getRequestDispatcher(forward);
                   
                   request.setAttribute("courses", dao.getAllCourses());
                   view.forward(request, response);
                   
                   break;
            }


        } catch (Exception e) {
            e.printStackTrace();
            // Handle SQLException
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Course course = new Course();

        course.setCoursename(request.getParameter("coursename"));
        course.setFees(Integer.parseInt(request.getParameter("fees")));
        course.setCategory(request.getParameter("category"));
        course.setDuration(Integer.parseInt(request.getParameter("duration")));

        String courseId = request.getParameter("courseid");

        if (courseId == null || courseId.isEmpty()) {
            dao.addCourse(course);
        } else {
            course.setCourseid(Integer.parseInt(courseId));
            dao.updateCourse(course);
            
            RequestDispatcher list = request.getRequestDispatcher("view.jsp");
            
            request.setAttribute("courses", dao.getAllCourses());
            list.forward(request, response);
        }

        // Redirect to doGet for displaying updated course list
        response.sendRedirect(request.getContextPath() + "/CourseServlet");
    }
}
