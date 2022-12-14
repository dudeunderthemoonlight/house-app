package com.example.houseapp.newrequestscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.houseapp.AppContainer
import com.example.houseapp.MyApplication
import com.example.houseapp.R
import com.example.houseapp.data.remote.UserRequests
import com.example.houseapp.listscreen.RequestsViewModel
import kotlinx.android.synthetic.main.fragment_create_request.view.*
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.SQLException

/**
 * Форма создания новой заявки
 */
class CreateRequestView : Fragment() {
    private lateinit var viewOfLayout: View
    private lateinit var appContainer: AppContainer
    private val userViewModel: RequestsViewModel by activityViewModels { appContainer.requestsViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_create_request, container, false)
        viewOfLayout.elevatedButton.setOnClickListener {
            val userId = userViewModel.userId
            val selectedType = viewOfLayout.selectType.text.toString()
            val description = viewOfLayout.descriptionInputField.text.toString()
            if (selectedType.isEmpty() || description.isEmpty()) {
                Toast.makeText(activity, "Please fill all fields before send a request", Toast.LENGTH_LONG).show()
            }
            else if (description.length > 300) {
                Toast.makeText(activity, "Too large message", Toast.LENGTH_LONG).show()
            }
            else {
                try {
                    transaction {
                        addLogger()
                        UserRequests.insert {
                            it[UserRequests.userId] = userId
                            it[problemType] = selectedType
                            it[UserRequests.description] = description
                        }
                        Toast.makeText(activity, "Your request has been sent successfully", Toast.LENGTH_LONG).show()
                    }
                } catch (e: SQLException) {
                    println(e)
                    Toast.makeText(activity, "Check your Internet connection", Toast.LENGTH_LONG).show()
                }
            }
        }
        return viewOfLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appContainer = (requireActivity().application as MyApplication).appContainer
        (activity as AppCompatActivity).supportActionBar?.title = "Create new request"
    }
}