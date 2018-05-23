package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BillDAO;
import dao.BillDetailsDAO;
import model.Bill;
import model.BillDetails;
import model.Cart;
import model.Item;
import model.Product;
import model.User;

/**
 * Servlet implementation class checkout
 */
@WebServlet("/checkout")
public class checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	BillDAO billDao = new BillDAO();
	BillDetailsDAO billDetailsDAO = new BillDetailsDAO();
	DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();

	public checkout() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");

		String payment = request.getParameter("payment");
		String address = request.getParameter("address");
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		User user = (User) session.getAttribute("user");

			try {

				long Id = new Date().getTime();
				Calendar calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_MONTH) ;
				int month = calendar.get(Calendar.MONTH) + 1;
				int year = calendar.get(Calendar.YEAR);
				Bill bill = new Bill();

				bill.setIdOfBill(Id);
				bill.setIdOfUser(user.getId());
				bill.setTotal(cart.total());
				bill.setPayment(payment);
				bill.setAddress(address);
				bill.setDate(new Timestamp(new Date().getTime()));

				billDao.addBill(bill);

				for (Map.Entry<String, Item> list : cart.getCartItems().entrySet()) {
					billDetailsDAO.addDetailsBill(new BillDetails(Id, list.getValue().getProduct().getMaSanPham(),
							list.getValue().getProduct().getTenSanPham(), list.getValue().getQuantity(),
							list.getValue().getProduct().getGia(),day,month,year));
				}
				cart = new Cart();
				session.setAttribute("cart", cart);

			} catch (Exception e) {
				e.printStackTrace();
			}
			response.sendRedirect("index.jsp");
		}
}
