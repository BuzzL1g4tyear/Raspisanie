package com.example.Ras.UI.messUI

import com.example.Ras.R
import com.example.Ras.Utils.createToast
import kotlinx.android.synthetic.main.fragment_single_chat.*

class SingleChatFragment : BaseFragment(R.layout.fragment_single_chat) {
    override fun onResume() {
        super.onResume()

        chat_send_message.setOnClickListener {
            val inputMessage = message_chat.text.toString()
            if (inputMessage.isEmpty()) {
                createToast("Пусто")
            } else {
//                sendMessage(inputMessage,)
            }
        }
    }
}