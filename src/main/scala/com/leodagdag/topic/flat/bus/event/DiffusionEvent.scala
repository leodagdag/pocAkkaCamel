package com.leodagdag.topic.flat.bus.event

import com.leodagdag.topic.flat.data.Trade

case class DiffusionEvent(channel : String, message: Trade) {}

