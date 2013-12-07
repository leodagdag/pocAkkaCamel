package com.leodagdag.flatTopic.bus.event

import com.leodagdag.flatTopic.data.Trade



case class DiffusionEvent(channel : String, message: Trade) {}

