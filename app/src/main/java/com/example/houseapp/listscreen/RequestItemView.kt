package com.example.houseapp.listscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.houseapp.AppContainer
import com.example.houseapp.MyApplication
import com.example.houseapp.R
import com.example.houseapp.listscreen.RequestAdapter.Companion.REQUEST_KEY

/**
 * Фрагмент отображает выбранную заявку из списка заявок
 */
class RequestItemView : Fragment() {

    private lateinit var appContainer: AppContainer
    private val viewModel: RequestItemViewModel by activityViewModels { appContainer.requestsViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_request_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appContainer = (requireActivity().application as MyApplication).appContainer

        val requestId = requireArguments().getInt(REQUEST_KEY)

        viewModel.getRequest(requestId).observe(viewLifecycleOwner) { requestItem ->
            view.findViewById<TextView>(R.id.info_message).text = requestItem.description
            view.findViewById<TextView>(R.id.info_problemType).text = requestItem.problemType
            view.findViewById<TextView>(R.id.info_status).text =
                if (requestItem.isDone) "Done" else "In progress"
        }
    }
}
