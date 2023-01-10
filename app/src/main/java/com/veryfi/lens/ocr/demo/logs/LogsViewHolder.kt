package com.veryfi.lens.ocr.demo.logs

import androidx.recyclerview.widget.RecyclerView
import com.veryfi.lens.ocr.demo.databinding.LogsListItemBinding

class LogsViewHolder(private val itemBinding: LogsListItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun onBind(log: Log) {
        itemBinding.logsTitle.text = log.title
        itemBinding.logsMessage.text = log.message
    }
}